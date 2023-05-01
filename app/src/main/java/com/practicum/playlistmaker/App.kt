package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    companion object {
        const val SAVE_SETTINGS ="save_settings"
        const val KEY_ACTIVITY_SETTINGS = "key_activity_settings"
        const val KEY_SAVED_SEARCH = "key_saved_search"
    }

    lateinit var sharedSettings:SharedPreferences


    override fun onCreate() {
        super.onCreate()

        sharedSettings = getSharedPreferences(SAVE_SETTINGS, MODE_PRIVATE)

        setNightModeThemeApp()
    }

    private fun setNightModeThemeApp(){

        when(sharedSettings.getString(KEY_ACTIVITY_SETTINGS,null)){
            "true" -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
            "false" -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
           }
        }

    fun saveModeNightThemeApp (isChecked: Boolean){
        if (isChecked){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
             sharedSettings.edit()
            .putString(KEY_ACTIVITY_SETTINGS,"true")
            .apply()
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
             sharedSettings.edit()
            .putString(KEY_ACTIVITY_SETTINGS, "false")
            .apply()
           }
        }
    }
