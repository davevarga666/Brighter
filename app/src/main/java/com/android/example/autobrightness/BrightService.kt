package com.android.example.autobrightness

import android.Manifest
import android.app.ActivityManager
import android.app.IntentService
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import java.util.*
import android.util.Log
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import timber.log.Timber
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.content.ContextCompat


class BrightService : IntentService("BrightService") {


    private var mHandler = Handler()
    private lateinit var mRunnable: Runnable
    private val defaultBrightness = 50
    private var systemBrightness: Int = 0
    var retrievedApp: String? = "not yt yet"



    fun isItYt() {
        if (retrievedApp == "com.google.android.youtube" && systemBrightness != 255) {
            setBrightness(255)
            Timber.i("brightness set to max")
            stopSelf()
        }
        if (retrievedApp != "com.google.android.youtube" && systemBrightness == 255)
            setBrightness(defaultBrightness)
    }

    override fun onHandleIntent(p0: Intent?) {
        //delayed Toast (or anything)
        Timber.i("service started")
        retrievedApp = retrieveAppInForeground()
        Timber.i("$retrievedApp")
        systemBrightness = Settings.System.getInt(this.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0
        )
        isItYt()

        //later
        //val gotIt: Boolean = p0!!.extras.getBoolean("passedOn")


//        mRunnable = Runnable() {
//            //kotlin.run {
//            //&& SYSTEM_UI_FLAG_FULLSCREEN == 1) {
//            if (isItYt) {
//                setBrightness(255)
//                Timber.i("brightness set to max")
//            } else
//                setBrightness(defaultBrightness)
//            Timber.i("brightness reduced")


        }
//        mHandler.postDelayed(this.mRunnable, 1000)
//        Timber.i("brightness handling timed")
//        val mainActivity: MainActivity = MainActivity()
//        if (mainActivity.thatsAllFolks == true)
//            stopSelf()



    private fun Context.setBrightness(value: Int) {
        Settings.System.putInt(
            this.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            value
        )
    }

    private fun retrieveAppInForeground(): String? {
        var currentApp: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val usm = this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val appList: List<UsageStats>?
            appList =
                usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)

            if (appList != null && appList.isNotEmpty()) {
                val sortedMap = TreeMap<Long, UsageStats>()
                for (usageStats in appList) {
                    sortedMap.put(usageStats.lastTimeUsed, usageStats)
                }
                currentApp = sortedMap.takeIf { it.isNotEmpty() }?.lastEntry()?.value?.packageName
            }
        } else {
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            @Suppress("DEPRECATION") //The deprecated method is used for devices running an API lower than LOLLIPOP
            currentApp = am.getRunningTasks(1)[0].topActivity.packageName
        }
        Log.e("ActivityTAG", "Application in foreground: " + currentApp)
        return currentApp
    }

    override fun onDestroy() {
        mRunnable = Runnable {
            val broadcastIntent = Intent(this, BrightServiceRestarterBroadcastReceiver::class.java)
            sendBroadcast(broadcastIntent)
            Timber.i("broadcast sent")
        }
        mHandler.postDelayed(this.mRunnable, 1000)
        super.onDestroy()
        Timber.i("service destroyed")
    }

}