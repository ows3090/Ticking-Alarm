package ows.kotlinstudy.ticking_alarm.data.db.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity


@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmEntity")
    fun getAll() : Flowable<List<AlarmEntity>>

    @Query("SELECT * FROM AlarmEntity WHERE hour=:hour AND minute=:minute")
    fun getAlarm(hour: Int, minute: Int) : AlarmEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAlarm(entity: AlarmEntity)

    @Update
    fun updateAlarm(entity: AlarmEntity)

    @Delete
    fun deleteAlarm(entity: AlarmEntity)
}