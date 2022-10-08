package ows.kotlinstudy.ticking_alarm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class MERIDIEM {
    ANTE, POST
}

@Entity(primaryKeys = ["minute", "second"])
data class AlarmModel(
    val meridiem: MERIDIEM,
    val minute: Int,
    val second: Int,
    val switchOn: Boolean = false
)
