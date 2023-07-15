package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPref: SharedPreferences):SettingsRepository {

    companion object {
        const val DARK_THEME_KEY = "dark_theme_key"
    }

    override fun getThemeDarkStatus(): Boolean = sharedPref.getBoolean(DARK_THEME_KEY,false)

    override fun setSavedAppTheme() {
        when(sharedPref.getBoolean(DARK_THEME_KEY,false)){
            true -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
            else -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
        }
    }

    override fun updateThemeDarkStatus(isChecked: Boolean?) {
        if (isChecked == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPref.edit()
                .putBoolean(DARK_THEME_KEY,true)
                .apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPref.edit()
                .putBoolean(DARK_THEME_KEY, false)
                .apply()
        }
    }
}