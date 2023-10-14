package com.practicum.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp()

    fun sharePlaylist(playlistInMessage: String)

    fun openTerms()

    fun writeSupport()
}