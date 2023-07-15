package com.practicum.playlistmaker.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  val viewModel: MainViewModel by viewModel()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setSavedAppTheme()


        val onClickButtonSearchListener: View.OnClickListener = object : View.OnClickListener {

            override fun onClick(v: View?) {

                val displaySearchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearchIntent)

            }
        }

        binding.searchButton.setOnClickListener(onClickButtonSearchListener)


        binding.mediaButton.setOnClickListener {

            val displayMediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(displayMediaIntent)

        }

        binding.settingsButton.setOnClickListener {

            val displaySettingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displaySettingsIntent)

        }
    }
}