package ows.kotlinstudy.ticking_alarm.data.db.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity


@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmEntity")
    fun getAll() : Flowable<List<AlarmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAlarm(entity: AlarmEntity)

    @Delete
    fun deleteAlarm(entity: AlarmEntity)
}