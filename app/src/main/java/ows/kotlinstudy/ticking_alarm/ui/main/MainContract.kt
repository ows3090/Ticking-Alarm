package ows.kotlinstudy.ticking_alarm.ui.main

import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView

interface MainContract {
    interface View<out PresenterT> : BaseView<PresenterT> {
        fun showAlarmList(list: List<AlarmModel>)
    }

    interface Presenter : BasePresenter {
        fun addAlarm(hour: Int, minute: Int)
    }
}