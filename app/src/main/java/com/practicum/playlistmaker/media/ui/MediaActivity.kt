package com.practicum.playlistmaker.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.media.ui.fragments.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager,lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayoutMedia,binding.viewPager){tab,position ->
             when(position){
                 0 -> tab.text = getString(R.string.favorite_tab)
                 1 -> tab.text = getString(R.string.playlist_tab)
             }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        tabMediator.attach()
    }



    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}