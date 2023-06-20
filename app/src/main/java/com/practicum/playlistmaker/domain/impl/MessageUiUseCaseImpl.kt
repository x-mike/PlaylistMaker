package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.MessageUiUseCase
import com.practicum.playlistmaker.presentation.api.ViewModel

class MessageUiUseCaseImpl(private val viewModel: ViewModel):MessageUiUseCase{

    override fun showToastError() {
       viewModel.showToast()
    }
}