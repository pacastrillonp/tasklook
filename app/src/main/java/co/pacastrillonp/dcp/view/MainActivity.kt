package co.pacastrillonp.dcp.view

import android.content.Intent
import android.os.Bundle
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

    private val accessibilitySettingsResult: Int = 0

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
            accessibilityService()
        })

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


    }


    private fun accessibilityService() {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivityForResult(intent, accessibilitySettingsResult)
        } catch (e: Exception) {
            //TODO: poner log
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == accessibilitySettingsResult) {
            mainActivityViewModel.accessibilityResult()
        }
    }


}
