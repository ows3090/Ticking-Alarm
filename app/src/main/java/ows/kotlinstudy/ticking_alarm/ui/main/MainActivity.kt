package ows.kotlinstudy.ticking_alarm.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ows.kotlinstudy.ticking_alarm.R
import ows.kotlinstudy.ticking_alarm.br.AlarmBroadcastReceiver
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.MERIDIEM
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.databinding.ActivityMainBinding
import ows.kotlinstudy.ticking_alarm.util.AlarmUtils
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*
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

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
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

    override fun switchOnAlarm(hour: Int, minute: Int) {
        Timber.d("switchOnAlarm $hour $minute")

        val alarmId = hour * 60 + minute
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent = Intent(this, AlarmBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if(AlarmUtils.isBeforeToday(calendar)){
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        Timber.d("switchOnAlarm ${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.MONTH)} ${calendar.get(Calendar.DAY_OF_MONTH)}" +
                " ${calendar.get(Calendar.HOUR_OF_DAY)} ${calendar.get(Calendar.MINUTE)} ${calendar.get(Calendar.SECOND)}")

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun switchOffAlarm(hour: Int, minute: Int) {
        Timber.d("switchOffAlarm")

    }
}
