package com.practicum.playlistmaker.player.ui.states

sealed class PlayerToastState{
    object NoneMessage: PlayerToastState()
    class ShowMessage(val message: String): PlayerToastState()
}
