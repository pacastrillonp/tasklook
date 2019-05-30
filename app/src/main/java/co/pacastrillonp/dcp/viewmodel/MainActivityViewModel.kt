package co.pacastrillonp.dcp.viewmodel

import android.content.Context
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

    //region enable or disable button
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

    //endregion

    // region change text button text field
    private val _buttonTextToShowOutput = MediatorLiveData<String>().apply {
        value = when {
            accessibilitySettingsIsEnable() -> "UNLOCK"
            else -> "LOCK"

        }
    }
    val buttonTextToShowOutput: LiveData<String> get() = _buttonTextToShowOutput

    //endregion

    //region click event
    private val _lockOutput = MediatorLiveData<Unit>()
    val lockOutput: LiveData<Unit> get() = _lockOutput

    fun lockClick() {
        _lockOutput.postValue(Unit)
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

    fun accessibilityResult() {
        if (accessibilitySettingsIsEnable()) {
            _buttonTextToShowOutput.postValue("UNLOCK")
        } else {
            _buttonTextToShowOutput.postValue("LOCK")
        }
    }

}