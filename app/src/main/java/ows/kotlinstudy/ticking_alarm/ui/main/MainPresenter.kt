package ows.kotlinstudy.ticking_alarm.ui.main

import androidx.room.RoomDatabase
import ows.kotlinstudy.ticking_alarm.data.db.AlarmDatabase
import ows.kotlinstudy.ticking_alarm.ui.base.BasePresenter
import ows.kotlinstudy.ticking_alarm.ui.base.BaseView
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val db: AlarmDatabase
): MainContract.Presenter {
    lateinit var view: MainContract.View<MainPresenter>

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(view: BaseView<BasePresenter>) {
        this.view = view as MainContract.View<MainPresenter>
    }
}