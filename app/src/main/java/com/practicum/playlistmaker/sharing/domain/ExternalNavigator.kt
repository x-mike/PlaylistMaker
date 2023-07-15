package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)

    fun openLink(link:String)

    fun sendEmail(email:String, subject:String, text:String)

    fun getShareAppLink(): String

    fun getSupportEmailData(): EmailData

    fun getTermsLink():String

}