package ows.kotlinstudy.ticking_alarm.data.db.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity


@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmEntity")
    fun getAll() : Single<List<AlarmEntity>>

    @Query("SELECT * FROM AlarmEntity WHERE hour=:hour AND minute=:minute")
    fun getAlarm(hour: Int, minute: Int) : Single<AlarmEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlarm(entity: AlarmEntity): Completable

    @Update
    fun updateAlarm(entity: AlarmEntity): Completable

    @Update
    fun updateAll(list: List<AlarmEntity>): Completable

    @Delete
    fun deleteAlarm(entity: AlarmEntity): Completable
}