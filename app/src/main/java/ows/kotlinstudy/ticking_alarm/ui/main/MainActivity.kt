package ows.kotlinstudy.ticking_alarm.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ows.kotlinstudy.ticking_alarm.br.AlarmReceiver
import ows.kotlinstudy.ticking_alarm.data.model.AlarmModel
import ows.kotlinstudy.ticking_alarm.databinding.ActivityMainBinding
import ows.kotlinstudy.ticking_alarm.ui.adapter.AlarmAdapter
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
    lateinit var adapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.onCreate(this)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        if(intent.getBooleanExtra(IS_ALARM_EVENT, false)) {
            val alarmId = intent.getIntExtra(ALARM_ID, 0)
            presenter.updateAlarm(alarmId / 60, alarmId % 60)
        }
        else presenter.init()
    }

    override fun onDestroy() {
        Timber.d("onDestory()")
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun initViews() = with(binding) {
        Timber.d("initViews()")
        binding.timePicker.apply {
            hour = LocalDateTime.now().hour
            minute = LocalDateTime.now().minute
        }
        binding.timeRecyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        Timber.d("bindViews()")
        addButton.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            presenter.insertAlarm(hour, minute)
        }
    }

    override fun showAlarmList(list: List<AlarmModel>) {
        Timber.d("showAlarmList() list: $list")
        adapter.setAlarmList(list)
        adapter.notifyDataSetChanged()
    }

    override fun switchOnAlarm(hour: Int, minute: Int) {
        Timber.d("switchOnAlarm hour: $hour minute: $minute")
        val alarmId = hour * 60 + minute
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = Intent(this, AlarmReceiver::class.java).apply { putExtra(ALARM_ID, alarmId) }.let { intent ->
            PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (AlarmUtils.isBeforeToday(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        Timber.d("switchOnAlarm ${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.MONTH)} ${calendar.get(Calendar.DAY_OF_MONTH)}" +
                    " ${calendar.get(Calendar.HOUR_OF_DAY)} ${calendar.get(Calendar.MINUTE)} ${calendar.get(Calendar.SECOND)}")

        Toast.makeText(this, "${calendar.get(Calendar.HOUR_OF_DAY)}시 ${calendar.get(Calendar.MINUTE)}에 알람이 울립니다.", Toast.LENGTH_SHORT).show()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    override fun switchOffAlarm(hour: Int, minute: Int) {
        Timber.d("switchOffAlarm $hour $minute")
        val alarmId = hour * 60 + minute
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_NO_CREATE)
        }
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val ALARM_ID = "ALARM_ID"
        const val IS_ALARM_EVENT = "IS_ALARM_EVENT"
    }
}
