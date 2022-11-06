package ows.kotlinstudy.ticking_alarm.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity

interface AlarmRepository {
    fun getAlarmList(): Single<List<AlarmEntity>>

    fun getInitAlarmList(): Single<List<AlarmEntity>>

    fun insertAlarm(hour: Int, minute: Int): Completable

    fun deleteAlarm(hour: Int, minute: Int): Single<AlarmEntity>

    fun updateAlarm(hour: Int, minute: Int): Single<AlarmEntity>
}