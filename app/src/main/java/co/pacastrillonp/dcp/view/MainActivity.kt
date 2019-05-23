package co.pacastrillonp.dcp.view

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.ActivityManager.*
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.pacastrillonp.dcp.MyDeviceAdminReceiver
import co.pacastrillonp.dcp.R
import co.pacastrillonp.dcp.databinding.ActivityMainBinding
import co.pacastrillonp.dcp.di.util.viewModelProvider
import co.pacastrillonp.dcp.viewmodel.MainActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainActivityViewModel: MainActivityViewModel

    lateinit var devicePolicyManager: DevicePolicyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = viewModelProvider(viewModelFactory)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            viewModel = mainActivityViewModel
            lifecycleOwner = this@MainActivity
        }
        mainActivityViewModel.lockOutput.observe(this, Observer {
            when {
                it -> when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> enableTaskLock()
                }else -> when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> disableTaskLock()
                }
            }

        })
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> requestFullScreenMode()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)

    fun enableTaskLock() {
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val deviceAdminReceiver = ComponentName(this, MyDeviceAdminReceiver::class.java)

        when {
            devicePolicyManager.isDeviceOwnerApp(this.packageName) -> {
                val packages = arrayOf(this.packageName)
                devicePolicyManager.setLockTaskPackages(deviceAdminReceiver, packages)

                when {
                    devicePolicyManager.isLockTaskPermitted((this.packageName)) -> {
                        startLockTask()
                        print("startLockTask")
                    }
                    else -> startLockTask()
                }
            }
            else -> startLockTask()
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun disableTaskLock() {
        if (isLockTaskModeEnable()) {
            stopLockTask()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun isLockTaskModeEnable(): Boolean {
        val activityManager: ActivityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activityManager.lockTaskModeState != LOCK_TASK_MODE_NONE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (activityManager.lockTaskModeState) {
                LOCK_TASK_MODE_NONE -> return false
                LOCK_TASK_MODE_LOCKED -> return true
                LOCK_TASK_MODE_PINNED -> return true
            }
        }
        return false
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun requestFullScreenMode() {
        window.decorView.systemUiVisibility.apply {
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }


}

