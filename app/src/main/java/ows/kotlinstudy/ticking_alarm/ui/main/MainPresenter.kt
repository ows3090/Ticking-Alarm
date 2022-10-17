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
    override fun onCreate(view: BaseView<BasePresenter>) {
        this.view = view as MainContract.View<MainPresenter>
        initAlarmList()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    override fun addAlarm(hour: Int, minute: Int) {
        Timber.d("addAlarm")
        val model = AlarmModel(
            alarmInfo = AlarmEntity(if (hour < 12) MERIDIEM.ANTE else MERIDIEM.POST, hour, minute, true),
            onClickEvent = { deleteAlarmModel(hour, minute) },
            onToggleEvent = { updateAlarmModel(hour, minute) }
        )
        addAlarmModel(model)
    }

    private fun initAlarmList() {
        Timber.d("initAlarmList")
        compositeDisposable.add(
            dao.getAll()
                .flatMap { list ->
                    dao.updateAll(list.map { it.copy(switchOn = false) })
                        .andThen(Single.just(list.map { it.copy(switchOn = false) }))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { showUpdateAlarmList() },
                    { error -> Timber.e(error) }
                )
        )
    }

    private fun addAlarmModel(alarmModel: AlarmModel) {
        Timber.d("addAlarmModel")
        compositeDisposable.add(
            dao.insertAlarm(alarmModel.alarmInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        showUpdateAlarmList()
                        view.switchOnAlarm(alarmModel.alarmInfo.hour, alarmModel.alarmInfo.minute)
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    private fun deleteAlarmModel(hour: Int, minute: Int) {
        Timber.d("deleteAlarmModel")
        compositeDisposable.add(
            dao.getAlarm(hour, minute)
                .flatMap { dao.deleteAlarm(it).andThen(Single.just(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        showUpdateAlarmList()
                        if(it.switchOn)  view.switchOffAlarm(hour, minute)
                    },
                    { error -> Timber.e(error)}
                )
        )
    }

    private fun updateAlarmModel(hour: Int, minute: Int) {
        Timber.d("updateAlarmModel")
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
                        showUpdateAlarmList()
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

    private fun showUpdateAlarmList() {
        Timber.d("showUpdateAlarmList")
        compositeDisposable.add(
            dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        val alarmList = list.map {
                            AlarmModel(
                                it,
                                onClickEvent = { deleteAlarmModel(it.hour, it.minute) },
                                onToggleEvent = { updateAlarmModel(it.hour, it.minute) }
                            )
                        }.sortedWith(compareBy({ it.alarmInfo.hour }, { it.alarmInfo.minute }))
                        view.showAlarmList(alarmList)
                    },
                    { error -> Timber.e(error) }
                )
        )
    }
}