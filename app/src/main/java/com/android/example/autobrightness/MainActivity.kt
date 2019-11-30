package com.android.example.autobrightness

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.widget.Button
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    //get a list of foreground apps
    //filter for youtube ("com.google.android.youtube")
    //check status bar visibility on/off
    //https://stackoverflow.com/a/22300961

    //a BroadcastReceiver which will receive a signal when someone or something kills the service;
    // its role is to restart the service.

    //persistent bg service:
    //https://fabcirablog.weebly.com/blog/creating-a-never-ending-background-service-in-android

    //timing:
    //https://stackoverflow.com/a/55571076
    //https://docs.oracle.com/javase/8/docs/api/java/util/TimerTask.html


    //is the app (YT) app fg or bg:
    //https://stackoverflow.com/questions/54626475/android-how-to-check-application-is-running-or-not

    var thatsAllFolks = false
    var stopThis = false
        private set


    private lateinit var backIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContentView(R.layout.activity_main)

        //save for later

//        var sharedPref: SharedPreference = SharedPreference(this)
//        var resOne =
//            ContextCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS)

//        var resTwo = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
//        if (resOne == PackageManager.PERMISSION_GRANTED && resTwo == PackageManager.PERMISSION_GRANTED) {
//            sharedPref.save("permsGranted", true)
//        }
//        if (sharedPref.getValueBoolean("permsGranted", false)) {
//            val writeIntent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
//            startActivity(writeIntent)
//            Timber.i("Write access granted")
//            val usageIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//            startActivity(usageIntent)
//            Timber.i("Usage access granted")
//        }
        //stopThis = letsSave.getValueBoolean("stopThis")

        // RESTORE THIS IF PERMS NOT GRANTED!!!
//        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED))
//            {
//            val writeIntent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
//            startActivity(writeIntent)
//            Timber.i("Write access granted")
//            val usageIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//            startActivity(usageIntent)
//            Timber.i("Usage access granted")
//            }
        val start = findViewById(R.id.startButton) as Button
        val stop = findViewById(R.id.stopButton) as Button
        start.setOnClickListener {
            backIntent = Intent(this, BrightService::class.java)
            //backIntent.putExtra("passedOn", stopThis)
            startService(backIntent)
            finish()
            Timber.i("MA finished")
        }
        stop.setOnClickListener {
            finish()
        }
    }


    @Suppress("DEPRECATION") // Deprecated for third party Services.
    fun <T> Context.isServiceRunning(service: Class<T>) =
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }


    override fun onDestroy() {
        //investigate this
        stopService(backIntent)
        super.onDestroy()
        Timber.i("MA destroyed!")
    }
}



