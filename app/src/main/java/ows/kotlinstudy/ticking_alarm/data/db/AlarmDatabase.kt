package ows.kotlinstudy.ticking_alarm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao

@Database(entities = [AlarmModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AlarmDatabase: RoomDatabase() {
    abstract class alarmDao(): AlarmDao
}