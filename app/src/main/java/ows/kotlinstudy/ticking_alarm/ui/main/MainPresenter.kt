package ows.kotlinstudy.ticking_alarm.ui.main

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.data.repository.AlarmRepository
import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView
import timber.log.Timber
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val repository: AlarmRepository
) : MainContract.Presenter {
    private lateinit var view: MainContract.View<MainPresenter>
    private val compositeDisposable = CompositeDisposable()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(view: BaseView<out BasePresenter>) {
        Timber.d("onCreate()")
        this.view = view as MainContract.View<MainPresenter>
    }

    override fun onDestroy() {
        Timber.d("onDestory()")
        compositeDisposable.dispose()
    }

    override fun init() {
        Timber.d("init()")
        compositeDisposable.add(
            repository.getInitAlarmList()
                .subscribe(
                    { update() },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun insertAlarm(hour: Int, minute: Int) {
        Timber.d("addAlarm hour: $hour minute: $minute")
        compositeDisposable.add(
            repository.insertAlarm(hour, minute)
                .subscribe(
                    {
                        update()
                        view.switchOnAlarm(hour, minute)
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun updateAlarm(hour: Int, minute: Int) {
        Timber.d("updateAlarm hour: $hour minute: $minute")
        compositeDisposable.add(
            repository.updateAlarm(hour, minute)
                .subscribe(
                    { entity ->
                        update()
                        if (entity.switchOn) view.switchOnAlarm(entity.hour, entity.minute)
                        else view.switchOffAlarm(entity.hour, entity.minute)
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun deleteAlarm(hour: Int, minute: Int) {
        Timber.d("deleteAlarm hour: $hour minute: $minute")
        compositeDisposable.add(
            repository.deleteAlarm(hour, minute)
                .subscribe(
                    { entity ->
                        if (entity.switchOn) view.switchOffAlarm(entity.hour, entity.minute)
                        update()
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    override fun update() {
        Timber.d("update()")
        compositeDisposable.add(
            repository.getAlarmList()
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
}