package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.model.SettingsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getThemeSettings()

        viewModel.getThemeLiveData().observe(viewLifecycleOwner) {
            render(it)
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