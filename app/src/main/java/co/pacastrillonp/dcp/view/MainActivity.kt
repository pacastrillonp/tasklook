package co.pacastrillonp.dcp.view

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.pacastrillonp.dcp.databinding.ActivityMainBinding
import co.pacastrillonp.dcp.di.util.viewModelProvider
import co.pacastrillonp.dcp.viewmodel.MainActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainActivityViewModel: MainActivityViewModel


    private var serviceBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = viewModelProvider(viewModelFactory)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, co.pacastrillonp.dcp.R.layout.activity_main
        ).apply {
            viewModel = mainActivityViewModel
            lifecycleOwner = this@MainActivity
        }
        mainActivityViewModel.lockOutput.observe(this, Observer {
            when {
//                it -> accessibilityService()

            }
        })

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }




    private val applicationForegroundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            serviceBound = false
        }
    }


}

