package co.pacastrillonp.dcp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import co.pacastrillonp.dcp.view.MainActivity


class ApplicationForegroundService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        //Configure these here for compatibility with API 13 and below.
        val config = AccessibilityServiceInfo()
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC

        if (Build.VERSION.SDK_INT >= 16)
        //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS

        serviceInfo = config
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event!!.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.packageName != null && event.className != null) {
                val componentName = ComponentName(
                    event.packageName.toString(),
                    event.className.toString()
                )
                val activityInfo = tryGetActivity(componentName)
                val isActivity = activityInfo != null
                if (isActivity)
                    Log.i("CurrentActivity", componentName.flattenToShortString())
                if (componentName.flattenToShortString() == "com.android.settings/.Settings") {
                    val launchIntent = packageManager.getLaunchIntentForPackage("co.arkbox")
                    if (launchIntent != null) {
                        startActivity(launchIntent)//null pointer check in case package name was not found
                    }
                }
            }
        }
    }

    private fun tryGetActivity(componentName: ComponentName): ActivityInfo? {
        return try {
            packageManager.getActivityInfo(componentName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }

    }

    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var applicationForegroundServiceMessenger: Messenger


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate() {
        applicationForegroundServiceMessenger = Messenger(IncomingHandler())
        try {
            val handler = Handler()
            val runnableCode = object : Runnable {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
                override fun run() {
                    handler.postDelayed(this, 5000)
                    getPackages()
//                    val current = getCurrentForeGroundApplication()
//                    val current = getCurrentApp()
                    val current = printForegroundTask()
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


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun printForegroundTask(): String {
        return ""
    }

    @SuppressLint("HandlerLeak")
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

            }
        }
    }
}