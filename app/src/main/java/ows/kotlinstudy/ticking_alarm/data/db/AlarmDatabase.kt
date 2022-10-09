package ows.kotlinstudy.ticking_alarm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao

@Database(entities = [AlarmEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AlarmDatabase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}