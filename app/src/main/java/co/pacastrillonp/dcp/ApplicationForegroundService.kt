package co.pacastrillonp.dcp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.*
import android.util.Log
import dagger.android.DaggerService


class ApplicationForegroundService : DaggerService() {
    private lateinit var applicationForegroundServiceMessenger: Messenger


    override fun onBind(intent: Intent?): IBinder? = applicationForegroundServiceMessenger.binder


    override fun onCreate() {
        applicationForegroundServiceMessenger = Messenger(IncomingHandler())
        try {
            val handler = Handler()
            val runnableCode = object : Runnable {
                override fun run() {
                    handler.postDelayed(this, 5000)
                    getPackages()
//                    val current = getCurrentForeGroundApplication()
                    val current =  getCurrentApp()
                    Log.e("CURRENT", current)
                }
            }

            runnableCode.run()
        } catch (ex: Exception) {
            print(ex)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return Service.START_NOT_STICKY
    }

    private fun getPackages(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(mainIntent, 0)

    }


    @SuppressLint("ServiceCast")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getCurrentForeGroundApplication(): String {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        val task = tasks[0] // current task
        val rootActivity = task.baseActivity


        return rootActivity.packageName
    }


    private fun getCurrentApp(): String {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
// The first in the list of RunningTasks is always the foreground task.
        val foregroundTaskInfo = am.getRunningTasks(1)[0]
        val foregroundTaskPackageName = foregroundTaskInfo.topActivity.packageName
        val pm = packageManager
        val foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0)
        val foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString()

        return "$foregroundTaskAppName $foregroundTaskPackageName"
    }

    @SuppressLint("HandlerLeak")
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

            }
        }
    }
}