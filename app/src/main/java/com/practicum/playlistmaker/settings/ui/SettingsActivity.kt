package com.practicum.playlistmaker.settings.ui

import android.annotation.SuppressLint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.model.SettingsState

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getThemeSettings()

        viewModel.getThemeLiveData().observe(this) {
            render(it)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.appShare.setOnClickListener {

            viewModel.shareApp()

        }


        binding.supportWrite.setOnClickListener {

            viewModel.writeSupport()

        }


        binding.agreement.setOnClickListener {

            viewModel.openTerms()
        }


        binding.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->

            viewModel.updateThemeSetting(isChecked)
        }

    }


    private fun getThemeSettings() {
        viewModel.getThemeSettings()
    }

    private fun render(state: SettingsState) {
        binding.switchButton.isChecked = state == SettingsState.NIGHT_MODE
    }
}