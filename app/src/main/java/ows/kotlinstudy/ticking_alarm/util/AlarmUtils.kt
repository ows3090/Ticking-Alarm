package ows.kotlinstudy.ticking_alarm.util

import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import java.text.SimpleDateFormat
import java.util.*

object AlarmUtils {
    fun isBeforeToday(calendar: Calendar): Boolean{
        val today = Calendar.getInstance()
        return today > calendar
    }
}
