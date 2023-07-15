package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(val context: Context):SettingsRepository {

    companion object {
        const val SAVE_SETTINGS ="save_settings"
        const val DARK_THEME_KEY = "dark_theme_key"
    }

    val sharedSettings = context.getSharedPreferences(SAVE_SETTINGS, MODE_PRIVATE)


    override fun getThemeDarkStatus(): Boolean = sharedSettings.getBoolean(DARK_THEME_KEY,false)


    override fun setSavedAppTheme() {
        when(sharedSettings.getBoolean(DARK_THEME_KEY,false)){
            true -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
            else -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
        }
    }

    override fun updateThemeDarkStatus(isChecked: Boolean?) {
        if (isChecked == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedSettings.edit()
                .putBoolean(DARK_THEME_KEY,true)
                .apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedSettings.edit()
                .putBoolean(DARK_THEME_KEY, false)
                .apply()
        }
    }

}