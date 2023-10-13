package com.practicum.playlistmaker.player.ui.states

sealed class ToastState{
    object NoneMessage: ToastState()
    class ShowMessage(val message: String): ToastState()
}
