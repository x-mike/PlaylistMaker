package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context:Context,
                            private val intent: Intent) : ExternalNavigator {

    override fun openLink(link: String) {
        intent.apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(link)
        }

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun share(info: String) {

        intent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,info)
            type = "text/plain"
        }

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun sendEmail(email: String, subject: String, text: String) {

        intent.apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse(context.getString(R.string.mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT,subject)
            putExtra(Intent.EXTRA_TEXT,text)
        }

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

      }

    override fun getShareAppLink(): String = context.getString(R.string.pr_ya_android_developer)

    override fun getSupportEmailData(): EmailData = EmailData(
        context.getString(R.string.developer_mail),
        context.getString(R.string.subject_mail),
        context.getString(R.string.body_mail)
    )

    override fun getTermsLink(): String = context.getString(R.string.pr_offer)

}