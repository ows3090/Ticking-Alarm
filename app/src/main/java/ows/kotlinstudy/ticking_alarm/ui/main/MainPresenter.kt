package ows.kotlinstudy.ticking_alarm.ui.main

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView
import timber.log.Timber
import java.sql.Time
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val dao: AlarmDao
) : MainContract.Presenter {
    private lateinit var view: MainContract.View<MainPresenter>
    private val compositeDisposable = CompositeDisposable()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(view: BaseView<out BasePresenter>) {
        Timber.d("onCreate()")
        this.view = view as MainContract.View<MainPresenter>
        init()
    }

    override fun onDestroy() {
        Timber.d("onDestory()")
        compositeDisposable.dispose()
    }

    override fun init() {
        Timber.d("init()")
        compositeDisposable.add(
            dao.getAll()
                .flatMap { list ->
                    dao.updateAll(list.map { it.copy(switchOn = false) })
                        .andThen(Single.just(list.map { it.copy(switchOn = false) }))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { selectAll() },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun insertAlarm(hour: Int, minute: Int) {
        Timber.d("addAlarm hour: $hour minute: $minute")
        val model = AlarmModel(
            alarmInfo = AlarmEntity(if (hour < 12) MERIDIEM.ANTE else MERIDIEM.POST, hour, minute, true),
            onClickEvent = { deleteAlarm(hour, minute) },
            onToggleEvent = { updateAlarm(hour, minute) }
        )

        compositeDisposable.add(
            dao.insertAlarm(model.alarmInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        selectAll()
                        view.switchOnAlarm(model.alarmInfo.hour, model.alarmInfo.minute)
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun updateAlarm(hour: Int, minute: Int) {
        Timber.d("updateAlarm hour: $hour minute: $minute")
        compositeDisposable.add(
            dao.getAlarm(hour, minute)
                .flatMap { entity ->
                    dao.updateAlarm(entity.copy(switchOn = entity.switchOn.not()))
                        .andThen(Single.just(entity.copy(switchOn = entity.switchOn.not())))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { entity ->
                        selectAll()
                        if (entity.switchOn) {
                            view.switchOnAlarm(entity.hour, entity.minute)
                        } else {
                            view.switchOffAlarm(entity.hour, entity.minute)
                        }
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun deleteAlarm(hour: Int, minute: Int) {
        Timber.d("deleteAlarm hour: $hour minute: $minute")
        compositeDisposable.add(
            dao.getAlarm(hour, minute)
                .flatMap { dao.deleteAlarm(it).andThen(Single.just(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        selectAll()
                        if(it.switchOn)  view.switchOffAlarm(hour, minute)
                    },
                    { error -> Timber.e(error)}
                )
        )
    }

    override fun selectAll() {
        Timber.d("selectAll()")
        compositeDisposable.add(
            dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        val alarmList = list.map {
                            AlarmModel(
                                it,
                                onClickEvent = { deleteAlarm(it.hour, it.minute) },
                                onToggleEvent = { updateAlarm(it.hour, it.minute) }
                            )
                        }.sortedWith(compareBy({ it.alarmInfo.hour }, { it.alarmInfo.minute }))
                        view.showAlarmList(alarmList)
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

//    private fun initSavedAlarmList() {
//        Timber.d("initSavedAlarmList")
//        compositeDisposable.add(
//            dao.getAll()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { list ->
//                        showUpdateAlarmList()
//                        list.filter { it.switchOn }.forEach{ view.switchOnAlarm(it.hour, it.minute)}
//                    },
//                    { error -> Timber.e(error)}
//                )
//        )
//    }
}