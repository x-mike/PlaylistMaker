package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackArrow = findViewById<Button>(R.id.back_button)

        buttonBackArrow.setOnClickListener{
            finish()
        }


        val buttonAppShare = findViewById<Button>(R.id.app_share)

        buttonAppShare.setOnClickListener {

            val intentShareApp = Intent(Intent.ACTION_SEND)

            intentShareApp.putExtra(Intent.EXTRA_TEXT,getString(R.string.pr_ya_android_developer))
            intentShareApp.type = "text/plain"

            startActivity(intentShareApp)
        }

        val buttonSupportWrite = findViewById<Button>(R.id.support_write)

        buttonSupportWrite.setOnClickListener {

            val intentSupportWrite = Intent(Intent.ACTION_SENDTO,Uri.parse(getString(R.string.mailto)))

            intentSupportWrite.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_mail)))
            intentSupportWrite.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.subject_mail))
            intentSupportWrite.putExtra(Intent.EXTRA_TEXT,getString(R.string.body_mail))

            startActivity(intentSupportWrite)
        }

        val buttonAgreement = findViewById<Button>(R.id.agreement)

        buttonAgreement.setOnClickListener {

            val link = getString(R.string.pr_offer)
            val intentAgreement = Intent(Intent.ACTION_VIEW, Uri.parse(link))

            startActivity(intentAgreement)
        }
    }
 }