package ows.kotlinstudy.ticking_alarm.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import ows.kotlinstudy.ticking_alarm.data.db.AlarmModel

@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmModel")
    fun getAll() : List<AlarmModel>
}