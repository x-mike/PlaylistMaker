package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackArrow = findViewById<Button>(R.id.back_button)

        buttonBackArrow.setOnClickListener{
            val displayMainIntent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(displayMainIntent)
        }

    }
}