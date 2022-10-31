package ows.kotlinstudy.ticking_alarm.ui.main

import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView

interface MainContract {
    interface View<Presenter> : BaseView<Presenter> {
        fun showAlarmList(list: List<AlarmModel>)
        fun switchOnAlarm(hour: Int, minute: Int)
        fun switchOffAlarm(hour: Int, minute: Int)
    }

    interface Presenter : BasePresenter {
        fun init()
        fun insertAlarm(hour: Int, minute: Int)
        fun updateAlarm(hour: Int, minute: Int)
        fun deleteAlarm(hour: Int, minute: Int)
        fun selectAll()
    }
}