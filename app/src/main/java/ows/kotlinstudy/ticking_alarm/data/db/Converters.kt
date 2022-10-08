package ows.kotlinstudy.ticking_alarm.data.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toInt(meridiem: MERIDIEM) = when(meridiem){
        MERIDIEM.ANTE -> 0
        MERIDIEM.POST -> 1
    }

    @TypeConverter
    fun toMeridiem(n: Int) = when(n){
        0 -> MERIDIEM.ANTE
        else -> MERIDIEM.POST
    }
}