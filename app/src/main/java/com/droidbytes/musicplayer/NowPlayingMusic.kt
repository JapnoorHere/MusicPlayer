package com.droidbytes.musicplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.droidbytes.musicplayer.databinding.FragmentNowPlayingMusicBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NowPlayingMusic : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentNowPlayingMusicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNowPlayingMusicBinding.inflate(layoutInflater,container,false)

        binding.playPauseButton.setOnClickListener {

        }

        return binding.root
    }


}