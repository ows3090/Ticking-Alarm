package ows.kotlinstudy.ticking_alarm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class MERIDIEM {
    ANTE, POST
}

@Entity(primaryKeys = ["hour","minute"])
data class AlarmEntity(
    val meridiem: MERIDIEM,
    val hour: Int,
    val minute: Int,
    val switchOn: Boolean = false
)
