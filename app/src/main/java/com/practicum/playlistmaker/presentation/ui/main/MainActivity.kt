package com.practicum.playlistmaker.presentation.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.practicum.playlistmaker.presentation.ui.media.MediaActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity
import com.practicum.playlistmaker.presentation.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search_button)

        val onClickButtonSearchListener: View.OnClickListener = object : View.OnClickListener {

            override fun onClick(v: View?) {

                val displaySearchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearchIntent)

            }
        }

        buttonSearch.setOnClickListener(onClickButtonSearchListener)

        val buttonMedia = findViewById<Button>(R.id.media_button)

        buttonMedia.setOnClickListener {

            val displayMediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(displayMediaIntent)

        }

        val buttonSettings = findViewById<Button>(R.id.settings_button)

        buttonSettings.setOnClickListener {

            val displaySettingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displaySettingsIntent)

        }
    }
}