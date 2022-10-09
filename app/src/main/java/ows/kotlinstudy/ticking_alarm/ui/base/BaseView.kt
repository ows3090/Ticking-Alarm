package ows.kotlinstudy.ticking_alarm.ui.base

interface BaseView<out PresenterT>{
    val presenter: PresenterT
}