package com.practicum.playlistmaker.main.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.util.ArgsTransfer

class RootActivity : AppCompatActivity(), ArgsTransfer {

    companion object {
        const val BUNDLE_ARGS = "args"
    }

    private var bundleArgs: Bundle? = null

    private lateinit var binding: ActivityRootBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment, R.id.playlistTracksFragment, R.id.updatePlaylistFragment -> {
                    binding.bottomNavView.isVisible = false
                }

                else -> {
                    binding.bottomNavView.isVisible = true
                }
            }
        }
    }

    override fun postArgs(args: Bundle?) {
        bundleArgs = args
    }

    override fun getArgs(): Bundle? {
        return bundleArgs
    }

}
