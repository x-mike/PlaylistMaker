package com.practicum.playlistmaker.favorite.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.adapter.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    private lateinit var binding: FragmentFavoriteTracksBinding

    private val listTrack = ArrayList<Track>()
    private lateinit var tracksAdapter: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTracksBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteTracksViewModel.getLiveData().observe(viewLifecycleOwner){
            render(it)
        }

         tracksAdapter = TracksAdapter(listTrack,{lifecycleScope},setListenerForAdapter())
         binding.recyclerDbSearch.adapter = tracksAdapter
         binding.recyclerDbSearch.layoutManager = LinearLayoutManager(requireContext())

         favoriteTracksViewModel.getTracksList()
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.getTracksList()
    }

    private fun setListenerForAdapter(): TracksAdapter.TrackClickListener {
        return object : TracksAdapter.TrackClickListener {

            override fun onClickView(track: Track) {

                val intent = Intent(requireContext(), PlayerActivity::class.java)
                intent.putExtra("dataTrack", TrackPlr.mappingTrack(track))
                requireContext().startActivity(intent)
            }
        }
    }


    private fun render(state:FavoriteState){
        when(state){
            is FavoriteState.ContentFavorite -> showContent(state.listTack)
            is FavoriteState.EmptyFavorite -> showEmpty()
        }
    }

    private fun showContent(list:List<Track>){
        listTrack.clear()
        listTrack.addAll(list)
        binding.noFoundLinearLayout.visibility = View.GONE

        tracksAdapter.notifyDataSetChanged()
        binding.foundTracksDb.visibility = View.VISIBLE
   }

    private fun showEmpty(){
        binding.foundTracksDb.visibility = View.GONE
        binding.noFoundLinearLayout.visibility = View.VISIBLE
    }


}