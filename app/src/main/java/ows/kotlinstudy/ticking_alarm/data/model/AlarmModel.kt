package ows.kotlinstudy.ticking_alarm.data.model

import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity

data class AlarmModel(
    val alarmInfo: AlarmEntity,
    val onItemClickEvent: () -> Unit
)
