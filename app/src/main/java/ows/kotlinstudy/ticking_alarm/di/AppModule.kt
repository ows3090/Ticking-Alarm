package ows.kotlinstudy.ticking_alarm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ows.kotlinstudy.ticking_alarm.data.db.AlarmDatabase
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): AlarmDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            AlarmDatabase::class.java,
            "alarm.db"
        ).build()
    }

    @Provides
    fun provideAlarmDao(db: AlarmDatabase): AlarmDao{
        return db.alarmDao()
    }
}