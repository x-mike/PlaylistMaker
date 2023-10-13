package com.practicum.playlistmaker.playlist.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.util.ArgsTransfer
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.playlist.domain.models.states.StateAddDb
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.viewModels.NewPlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : Fragment() {

    companion object {

        const val IMAGE_PL = "IMAGE"
        private var creationWithoutGraph = false

        fun newInstance(flagWithoutGraph:Boolean): NewPlaylistFragment {

            creationWithoutGraph = flagWithoutGraph

            return NewPlaylistFragment()
        }
    }

    open val newPlaylistViewModel: NewPlaylistViewModel by viewModel()

    open lateinit var binding: FragmentNewPlaylistBinding

    private val requester = PermissionRequester.instance()

     var uriImageTemp: Uri? = null
     var playlistNameTemp: String? = null
     var descriptionPlaylistTemp: String? = null


     val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {

        if (it != null) {
            val roundCorners = RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_button_low))
            val options = RequestOptions().transform(CenterCrop(),roundCorners)

            Glide.with(this)
                .load(it.toString())
                .placeholder(R.drawable.add_photo)
                .apply(options)
                .into(binding.imageNewPl)

            uriImageTemp = it
        } else {
            Log.d("Image",R.string.image_not_select.toString())
        }
    }


     open fun checkPermission(): Boolean {
        val permissionProvided =
            ContextCompat.checkSelfPermission(requireContext(), getCheckedStorageConst())

        return (permissionProvided == PackageManager.PERMISSION_GRANTED)
    }

    private fun getCheckedStorageConst(): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogExit = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_playlist_dialog)
            .setMessage(R.string.message_playlist_dialog)
            .setNeutralButton(R.string.cancel_playlist_dialog){dialog,which ->

            }
            .setPositiveButton(R.string.complete_playlist_dialog){dialog,which ->

                backStackSelector()
            }

        newPlaylistViewModel.getLiveData().observe(viewLifecycleOwner){
            render(it)
        }

        binding.imageNewPl.setOnClickListener {

            if (!checkPermission()) {

                lifecycleScope.launch {
                    requester.request(getCheckedStorageConst()).collect {
                        when (it) {
                            is PermissionResult.Granted -> {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                            is PermissionResult.Denied.NeedsRationale -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.rationale_permission_on_storage),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is PermissionResult.Denied.DeniedPermanently -> {
                                val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.data = Uri.fromParts("package", requireContext().packageName, null)
                                requireContext().startActivity(intent)
                            }
                            is PermissionResult.Cancelled -> {
                                return@collect
                            }
                            is PermissionResult.Denied -> {
                                Log.d("Permission",R.string.permission_denied.toString())
                            }
                        }
                    }
                }

            } else {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        binding.backButtonNewPlaylist.setOnClickListener {
            if(uriImageTemp == null &&
                (playlistNameTemp == null || playlistNameTemp == "") &&
                (descriptionPlaylistTemp == null || descriptionPlaylistTemp == "")){

                   backStackSelector()

            }else {
                dialogExit.show()
            }
        }

        binding.inputNamePl.addTextChangedListener(getTextWatcherForName())
        binding.inputDescriptionPl.addTextChangedListener(getTextWatcherForDiscription())

        binding.buttonCreatePlaylist.setOnClickListener {
             newPlaylistViewModel.addPlaylist(Playlist(
                 playlistName = playlistNameTemp,
                 descriptionPlaylist = descriptionPlaylistTemp,
                 imageInStorage = uriImageTemp.toString()
                 ))
        }

        switchOnBackPressedDispatcher(true,dialogExit)

    }

    open fun switchOnBackPressedDispatcher(switch:Boolean,dialogExit: MaterialAlertDialogBuilder?){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(switch){

            override fun handleOnBackPressed() {
                if(uriImageTemp == null &&
                    (playlistNameTemp == null || playlistNameTemp == "") &&
                    (descriptionPlaylistTemp == null || descriptionPlaylistTemp == "")){

                    backStackSelector()

                }else {
                    dialogExit?.show()
                }
            }
        })
    }

    private fun backStackSelector(){

        if (creationWithoutGraph) {
            requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_player).isVisible = true
            parentFragmentManager.popBackStack()
            creationWithoutGraph = false
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null) {

            if(savedInstanceState.getString(IMAGE_PL) != "null") {
                val roundCorners = RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_button_low))
                val options = RequestOptions().transform(CenterCrop(),roundCorners)

                Glide.with(this)
                    .load(savedInstanceState.getString(IMAGE_PL))
                    .placeholder(R.drawable.add_photo)
                    .apply(options)
                    .into(binding.imageNewPl)

                uriImageTemp = savedInstanceState.getString(IMAGE_PL)!!.toUri()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(IMAGE_PL,uriImageTemp.toString())
    }

   override fun onStart() {
        super.onStart()
        if(creationWithoutGraph) requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_player).isVisible = false
    }


    private fun getTextWatcherForName(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonCreatePlaylist.isEnabled = s?.isNotEmpty() == true
                playlistNameTemp = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
    }

    private fun getTextWatcherForDiscription(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descriptionPlaylistTemp = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
    }

    open fun render(state: StateAddDb){
        when(state){
            is StateAddDb.Error -> {Log.e("ErrorAddPlaylist",R.string.error_add_playlist.toString())}
            is StateAddDb.NoError -> {

                if (creationWithoutGraph) {
                    (requireActivity() as ArgsTransfer)
                        .postArgs(bundleOf(Pair(PlayerActivity.BUNDLE_ARGS,playlistNameTemp)))
                    requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_player).isVisible = true
                    creationWithoutGraph = false
                    parentFragmentManager.popBackStack()
                }
                else {
                    (requireActivity() as ArgsTransfer)
                        .postArgs(bundleOf(Pair(RootActivity.BUNDLE_ARGS,playlistNameTemp)))
                    findNavController().popBackStack()
                }
            }
            is StateAddDb.Match -> {Log.e("ErrorAddPlaylist", getString(R.string.error_match_playlist))}
            is StateAddDb.NoData->{
                //State for Single Live Event, show Toast and other way
            }
        }
    }

}