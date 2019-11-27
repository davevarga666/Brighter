package com.android.example.autobrightness

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class BrightServiceRestarterBroadcastReceiver: BroadcastReceiver() {
    val mainActivity: MainActivity = MainActivity()
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("Service stops! Oooooooppppsssss!!!!!")
        //if (mainActivity.stopThis != true) {
            context?.startService(Intent(context, BrightService::class.java))
            Timber.i("Service restart granted!")
        //}


    }


}