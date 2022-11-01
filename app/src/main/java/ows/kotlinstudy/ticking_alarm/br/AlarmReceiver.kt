package ows.kotlinstudy.ticking_alarm.br

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import ows.kotlinstudy.ticking_alarm.R
import ows.kotlinstudy.ticking_alarm.ui.main.MainActivity
import timber.log.Timber

class AlarmReceiver : BroadcastReceiver() {
    val ALAMR_CHANNEL_NAME = "TICKING-ALARM"
    val ALARM_CHANNEL_ID = "TICKING-ALARM's ID"
    val ALARM_DESCRIPTION = "THIS IS A TICKING-ALARM APP"
    var alarmId: Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        alarmId = intent.getIntExtra(MainActivity.ALARM_ID, 0)
        Timber.d("onReceive ${intent.action} $alarmId")

        createNotificationChannel(context)
        val builder = createNotification(context)
        startNotification(context, builder)
    }

    private fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = ALAMR_CHANNEL_NAME
            val descriptionText = ALARM_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ALARM_CHANNEL_ID, channelName, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("NewApi", "UnspecifiedImmutableFlag")
    private fun createNotification(context: Context): Notification.Builder{
        val intent = Intent(context, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(MainActivity.IS_ALARM_EVENT, true)
            putExtra(MainActivity.ALARM_ID, alarmId)
        }

        val pendingIntent = PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return Notification.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
            .setContentTitle("Ticking-Alarm")
            .setContentText("${alarmId.toInt()/60}시 ${alarmId.toInt()%60}분이 되었습니다.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    private fun startNotification(context: Context,builder: Notification.Builder){
        with(NotificationManagerCompat.from(context)){
            notify(alarmId.toInt(), builder.build())
        }
    }
}