package ows.kotlinstudy.ticking_alarm.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao
import timber.log.Timber
import javax.inject.Inject

class LocalAlarmRepository @Inject constructor(
    private val dao: AlarmDao
) : AlarmRepository {

    override fun getAlarmList(): Single<List<AlarmEntity>> {
        return dao.getAll()
            .doOnError { Timber.e(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getInitAlarmList(): Single<List<AlarmEntity>> {
        return dao.getAll()
            .flatMap { list ->
                dao.updateAll(list.map { it.copy(switchOn = false) })
                    .andThen(Single.just(list.map { it.copy(switchOn = false) }))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertAlarm(hour: Int, minute: Int): Completable {
        return dao.insertAlarm(
            AlarmEntity(
                if (hour < 12) MERIDIEM.ANTE else MERIDIEM.POST,
                hour,
                minute,
                true
            )
        )
            .doOnError { error -> Timber.e(error) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteAlarm(hour: Int, minute: Int): Single<AlarmEntity> {
        return dao.getAlarm(hour, minute)
            .flatMap { entity ->
                dao.deleteAlarm(entity).andThen(Single.just(entity))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateAlarm(hour: Int, minute: Int): Single<AlarmEntity> {
        return dao.getAlarm(hour, minute)
            .flatMap { entity ->
                dao.updateAlarm(entity.copy(switchOn = entity.switchOn.not()))
                    .andThen(Single.just(entity.copy(switchOn = entity.switchOn.not())))
            }
            .doOnError { error -> Timber.e(error) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}