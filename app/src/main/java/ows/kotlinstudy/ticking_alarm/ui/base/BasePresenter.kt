package ows.kotlinstudy.ticking_alarm.ui.base

interface BasePresenter {
    fun onCreate(view: BaseView<out BasePresenter>)
    fun onDestroy()
}