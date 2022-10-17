package ows.kotlinstudy.ticking_alarm.br

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import ows.kotlinstudy.ticking_alarm.ui.main.MainActivity
import ows.kotlinstudy.ticking_alarm.ui.main.MainAdapter
import timber.log.Timber

class AlarmBroadcastReceiver : BroadcastReceiver() {
    val ALAMR_CHANNEL_NAME = "TICKING-ALARM"
    val ALARM_CHANNEL_ID = "TICKING-ALARM's ID"
    val ALARM_DESCRIPTION = "THIS IS A TICKING-ALARM APP"

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("onReceive ${intent.action}")

        createNotificationChannel(context)
        createNotification(context)
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

    private fun createNotification(context: Context){
        val intent = Intent(context, MainActivity::class.java).apply{
            flags = Intent.FLAG_AC
        }
    }
}