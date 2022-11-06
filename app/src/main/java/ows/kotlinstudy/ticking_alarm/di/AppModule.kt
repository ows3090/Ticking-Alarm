package ows.kotlinstudy.ticking_alarm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ows.kotlinstudy.ticking_alarm.data.db.AlarmDatabase
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao
import ows.kotlinstudy.ticking_alarm.data.repository.AlarmRepository
import ows.kotlinstudy.ticking_alarm.data.repository.LocalAlarmRepository

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindRepository(localAlarmRepository: LocalAlarmRepository): AlarmRepository

    companion object {
        @Provides
        fun provideRoomDatabase(
            @ApplicationContext context: Context
        ): AlarmDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AlarmDatabase::class.java,
                "alarm.db"
            ).build()
        }

        @Provides
        fun provideAlarmDao(db: AlarmDatabase): AlarmDao {
            return db.alarmDao()
        }
    }
}