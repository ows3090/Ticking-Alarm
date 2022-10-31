package ows.kotlinstudy.ticking_alarm.br

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ows.kotlinstudy.ticking_alarm.data.db.AlarmEntity
import ows.kotlinstudy.ticking_alarm.data.db.dao.AlarmDao
import ows.kotlinstudy.ticking_alarm.ui.main.MainActivity
import ows.kotlinstudy.ticking_alarm.util.AlarmUtils
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class BootReceiver: BroadcastReceiver() {
    @Inject
    lateinit var dao: AlarmDao
    val compositeDisposable = CompositeDisposable()

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("onReceive")
        if(intent.action == "android.intent.action.BOOT_COMPLETED"){
            initAlarm(context)
        }
    }

    private fun initAlarm(context: Context){
        compositeDisposable.add(
            dao.getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { list -> setSavedAlarm(context, list)},
                    { error -> Timber.e(error)}
                )
        )
    }

    private fun setSavedAlarm(context: Context, list: List<AlarmEntity>){
        list.forEach { entity ->
            val alarmId = entity.hour * 60 + entity.minute
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val pendingIntent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(MainActivity.ALARM_ID, alarmId)
            }.let { intent ->
                PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, entity.hour)
                set(Calendar.MINUTE, entity.minute)
                set(Calendar.SECOND, 0)
            }

            if (AlarmUtils.isBeforeToday(calendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            Timber.d("switchOnAlarm ${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.MONTH)} ${calendar.get(
                Calendar.DAY_OF_MONTH)}" +
                    " ${calendar.get(Calendar.HOUR_OF_DAY)} ${calendar.get(Calendar.MINUTE)} ${calendar.get(
                        Calendar.SECOND)}")

            Toast.makeText(context, "${calendar.get(Calendar.HOUR_OF_DAY)}시 ${calendar.get(Calendar.MINUTE)}에 알람이 울립니다.", Toast.LENGTH_SHORT).show()
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}