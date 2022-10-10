package ows.kotlinstudy.ticking_alarm.ui.main

import androidx.room.RoomDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import ows.kotlinstudy.ticking_alarm.data.db.AlarmDatabase
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView
import timber.log.Timber
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val dao: AlarmDao
) : MainContract.Presenter {
    private lateinit var view: MainContract.View<MainPresenter>
    private val compositeDisposable = CompositeDisposable()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(view: BaseView<BasePresenter>) {
        this.view = view as MainContract.View<MainPresenter>
        getAlarmModelList()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    override fun addAlarm(hour: Int, minute: Int) {
        val model = AlarmModel(
            AlarmEntity(
                if (hour < 12) MERIDIEM.ANTE else MERIDIEM.POST,
                hour,
                minute,
                true
            ),
            onClickEvent = {
                deleteAlarmModel(hour, minute)
            },
            onToggleEvent = {
                updateAlarmModel(hour, minute)
            }
        )
        addAlarmModel(model)
    }

    private fun getAlarmModelList() {
        compositeDisposable.add(
            dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        view.showAlarmList(
                            list.map {
                                AlarmModel(
                                    it,
                                    onClickEvent = { deleteAlarmModel(it.hour, it.minute) },
                                    onToggleEvent = { updateAlarmModel(it.hour, it.minute)}
                                )
                            }
                        )
                    },
                    { error -> Timber.e(error) }
                )
        )
    }

    private fun addAlarmModel(alarmModel: AlarmModel) {
        compositeDisposable.add(
            Observable.just(alarmModel)
                .subscribeOn(Schedulers.io())
                .doFinally { getAlarmModelList() }
                .subscribe(
                    { model -> dao.addAlarm(model.alarmInfo) },
                    { error -> Timber.e(error) }
                )
        )
    }

    private fun deleteAlarmModel(hour: Int, minute: Int) {
        compositeDisposable.add(
            Observable.just(
                AlarmEntity(
                    if (hour < 12) MERIDIEM.ANTE else MERIDIEM.POST,
                    hour,
                    minute
                )
            )
                .subscribeOn(Schedulers.io())
                .doFinally { getAlarmModelList() }
                .subscribe(
                    { entity -> dao.deleteAlarm(entity) },
                    { error -> Timber.e(error) }
                )
        )
    }

    private fun updateAlarmModel(hour: Int, minute: Int) {
        compositeDisposable.add(
            Observable.just(Pair(hour, minute))
                .subscribeOn(Schedulers.io())
                .doFinally { getAlarmModelList() }
                .subscribe(
                    { pair ->
                        val entity = dao.getAlarm(pair.first, pair.second)
                        dao.updateaAlarm(entity.copy(switchOn = entity.switchOn.not()))
                    },
                    { error -> Timber.e(error) }
                )
        )
    }
}