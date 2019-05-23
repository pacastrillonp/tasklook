package co.pacastrillonp.dcp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {

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
        value = "LOCK"
    }
    val buttonActionOutput: LiveData<String> get() = _buttonActionOutput


    fun lockClick() {
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
}