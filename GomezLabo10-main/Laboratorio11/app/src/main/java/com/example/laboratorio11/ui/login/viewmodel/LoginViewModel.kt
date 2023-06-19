package com.example.laboratorio11.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.laboratorio11.RetrofitApplication
import com.example.laboratorio11.network.ApiResponse
import com.example.laboratorio11.repository.CredentialsRepository
import com.example.laboratorio11.ui.login.LoginUiStatus
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: CredentialsRepository) : ViewModel() {

    var email = MutableLiveData("")
    var password = MutableLiveData("")

    private val _status = MutableLiveData<LoginUiStatus>(LoginUiStatus.Resume)
    val status: MutableLiveData<LoginUiStatus>
        get() = _status

    private fun login(email: String, password: String) {
        // Create a coroutine to call the login function from the repository and
       // inside the coroutine set the value of the status

        viewModelScope.launch {
            _status.postValue(
                when (val respone = repository.login(email, password)){
                    is ApiResponse.Error -> LoginUiStatus.Error(respone.exception)
                    is ApiResponse.ErrorWithMessage -> LoginUiStatus.ErrorWithMessage(respone.message)
                    is ApiResponse.Success -> LoginUiStatus.Success(respone.data)
                }
            )
        }

    }

    fun onLogin() {
        // Call the validateData function and if the data is valid call the login function

        if(!validateData()){
            _status.value = LoginUiStatus.ErrorWithMessage("Wrong information")
            return
        }
        login(email.value!!, password.value!!)
    }

    private fun validateData(): Boolean {
        when {
            email.value.isNullOrEmpty() -> return false
            password.value.isNullOrEmpty() -> return false
        }
        return true
    }

    fun clearData() {
        email.value = ""
        password.value = ""
    }

    fun clearStatus() {
        _status.value = LoginUiStatus.Resume
    }

    companion object {
        // Create a LoginViewModel Factory
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as RetrofitApplication
                LoginViewModel(app.credentialsRepository)
            }
        }

    }
}