package ows.kotlinstudy.ticking_alarm.ui.main

import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView

interface MainContract {
    interface View: BaseView<Presenter>{

    }

    interface Presenter: BasePresenter{

    }
}