package ows.kotlinstudy.ticking_alarm.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import timber.log.Timber

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == "android.intent.action.BOOT_COMPLETED"){
            Timber.d("AlarmBroadcastReceiver")
            Toast.makeText(context, "알람이 울립니다!", Toast.LENGTH_SHORT).show()
        }
    }
}