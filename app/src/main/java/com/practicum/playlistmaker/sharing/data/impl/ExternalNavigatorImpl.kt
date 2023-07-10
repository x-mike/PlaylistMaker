package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(val context:Context) : ExternalNavigator {

    override fun openLink(link: String) {
        val intentAgreement = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        context.startActivity(intentAgreement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun shareLink(link: String) {
        val intentShareApp = Intent(Intent.ACTION_SEND)

        intentShareApp.putExtra(Intent.EXTRA_TEXT,link)
        intentShareApp.type = "text/plain"

        context.startActivity(intentShareApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun sendEmail(email: String, subject: String, text: String) {
        val intentSupportWrite = Intent(Intent.ACTION_SENDTO, Uri.parse(context.getString(R.string.mailto)))

        intentSupportWrite.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intentSupportWrite.putExtra(Intent.EXTRA_SUBJECT,subject)
        intentSupportWrite.putExtra(Intent.EXTRA_TEXT,text)

        context.startActivity(intentSupportWrite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun getShareAppLink(): String = context.getString(R.string.pr_ya_android_developer)

    override fun getSupportEmailData(): EmailData = EmailData(
        context.getString(R.string.developer_mail),
        context.getString(R.string.subject_mail),
        context.getString(R.string.body_mail)
    )

    override fun getTermsLink(): String = context.getString(R.string.pr_offer)

}