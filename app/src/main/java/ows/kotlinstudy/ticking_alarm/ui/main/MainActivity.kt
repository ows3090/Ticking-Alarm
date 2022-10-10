package ows.kotlinstudy.ticking_alarm.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ows.kotlinstudy.ticking_alarm.R
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.databinding.ActivityMainBinding
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View<MainPresenter> {
    private lateinit var binding: ActivityMainBinding

    @Inject
    override lateinit var presenter: MainPresenter

    @Inject
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onCreate(this)
        initViews()
        bindViews()
    }

    private fun initViews() = with(binding) {
        binding.timePicker.apply {
            hour = LocalDateTime.now().hour
            minute = LocalDateTime.now().minute
        }
        binding.timeRecyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        addButton.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            presenter.addAlarm(hour, minute)
        }
    }

    override fun showAlarmList(list: List<AlarmModel>) {
        adapter.setAlarmList(list)
        adapter.notifyDataSetChanged()
    }
}
