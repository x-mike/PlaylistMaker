package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator): SharingInteractor {

    override fun shareApp() {
       externalNavigator.share(getShareLinkApp())
    }

    override fun sharePlaylist(playlistInMessage: String) {
        externalNavigator.share(playlistInMessage)
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun writeSupport() {
        val emailData = getEmailData()

        externalNavigator.sendEmail(emailData.email,
            emailData.subject,
            emailData.text)
    }

    private fun getEmailData():EmailData = externalNavigator.getSupportEmailData()

    private fun getShareLinkApp():String = externalNavigator.getShareAppLink()

    private fun getTermsLink():String = externalNavigator.getTermsLink()
}