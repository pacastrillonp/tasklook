package co.pacastrillonp.dcp.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import co.pacastrillonp.dcp.ApplicationForegroundService
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(private val context: Context) : ViewModel() {

    //Input
    val passwordTextInput = MediatorLiveData<String>()
    private val _passwordTextInput: LiveData<String> get() = passwordTextInput

    // Outputs

    private val _canLockOutput = MediatorLiveData<Boolean>().apply {
        value = false
        addSource(_passwordTextInput) {
            when {
                it == "123" -> value = true
                it != "123" -> value = false
            }
        }
    }

    val canLockOutput: LiveData<Boolean> get() = _canLockOutput

    private val _lockOutput = MediatorLiveData<Boolean>().apply {
        value = false
    }
    val lockOutput: LiveData<Boolean> get() = _lockOutput


    private val _buttonActionOutput = MediatorLiveData<String>().apply {
        value = when {
            accessibilitySettingsIsEnable() -> "LOCK"
            else -> "UNLOCK"

        }
    }
    val buttonActionOutput: LiveData<String> get() = _buttonActionOutput


    fun lockClick() {
        accessibilityService()

        when {
            !_lockOutput.value!! -> {
                _lockOutput.postValue(true)
                _buttonActionOutput.postValue("UNLOCK")
            }
            else -> {
                _lockOutput.postValue(false)
                _buttonActionOutput.postValue("LOCK")
            }
        }
    }


    private fun accessibilitySettingsIsEnable(): Boolean {
        var accessibilityEnabled = 0
        val service = context.packageName + "/" + ApplicationForegroundService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            //TODO: poner log
        }
        val stringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                stringColonSplitter.setString(settingValue)
                while (stringColonSplitter.hasNext()) {
                    val accessibilityService = stringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun accessibilityService() {
        try {
            val intent = Intent( Settings.ACTION_ACCESSIBILITY_SETTINGS)
            context.startActivity(intent)


        } catch (e: Exception) {
            print(e)

        }
    }
}