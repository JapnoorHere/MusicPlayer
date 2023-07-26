package com.droidbytes.musicplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
            if(MusicActivity.isPlaying) pauseMusic() else playMusic()
        }

        binding.next.setOnClickListener {
            nextSong()
            MusicActivity.musicService?.createMediaPlayer()
            setLayout()
            playMusic()
        }

        binding.prev.setOnClickListener {
            prevSong()
            MusicActivity.musicService?.createMediaPlayer()
            setLayout()
            playMusic()
        }

        binding.root.setOnClickListener {
            val intent = Intent(requireContext(),MusicActivity::class.java)
            intent.putExtra("class","NowPlaying")
            intent.putExtra("songPosition",MusicActivity.songPosition)
            intent.putExtra("songsList", MusicActivity.songsList)

            ContextCompat.startActivity(requireContext(),intent,null)
        }

        return binding.root
    }
    private fun playMusic() {
        MusicActivity.isPlaying = true
        MusicActivity.musicService!!.mediaPlayer.start()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
    }

    private fun pauseMusic() {
        MusicActivity.isPlaying = false
        MusicActivity.musicService!!.mediaPlayer.pause()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
    }

    override fun onResume() {
        super.onResume()
        if(MusicActivity.musicService != null){
            binding.root.visibility = View.VISIBLE
            binding.songName.isSelected = true
            setLayout()
            if(MusicActivity.isPlaying) binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
            else binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
        }
    }

    fun setLayout(){
        binding.singerName.text = MusicActivity.songsList[MusicActivity.songPosition].artist
        binding.songName.text = MusicActivity.songsList[MusicActivity.songPosition].name
        Glide.with(requireContext()).load(MusicActivity.songsList[MusicActivity.songPosition].albumArtUri)
            .into(binding.icon)
    }

}