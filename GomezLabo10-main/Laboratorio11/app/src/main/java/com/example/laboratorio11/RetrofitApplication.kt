package com.example.laboratorio11

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.laboratorio11.network.retrofit.RetrofitInstance
import com.example.laboratorio11.network.service.AuthService
import com.example.laboratorio11.repository.CredentialsRepository

class RetrofitApplication : Application() {

    // Create a variable to store the token in the SharedPreferences
    private val prefs: SharedPreferences by lazy{
        getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
    }

    // Get the API service from the Retrofit instance and set the token
    private fun getAPIService()= with(RetrofitInstance){
        setToken(getToken())
        getLoginService()

    }

    // Create a function to get the token

    fun getToken(): String = prefs.getString(USER_TOKEN, "" )!!

    val credentialsRepository: CredentialsRepository by lazy{
        CredentialsRepository(getAPIService())
    }

    // Initialize the repository

     fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    // Create a function to save the token in the SharedPreferences

    companion object {
        const val USER_TOKEN = "user_token"
    }
}