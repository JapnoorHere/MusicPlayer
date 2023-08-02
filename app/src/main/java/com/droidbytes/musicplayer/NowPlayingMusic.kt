package com.droidbytes.musicplayer

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.droidbytes.musicplayer.databinding.FragmentNowPlayingMusicBinding
import com.droidbytes.musicplayer.databinding.SongItemBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NowPlayingMusic : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentNowPlayingMusicBinding
    lateinit var songsListActivity: SongsListActivity

    companion object{
        var vibrantColor : Int =0
    }

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

        binding = FragmentNowPlayingMusicBinding.inflate(layoutInflater, container, false)
        songsListActivity = activity as SongsListActivity
        binding.playPauseButton.setOnClickListener {
            if (MusicActivity.isPlaying) pauseMusic() else playMusic()
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
            val intent = Intent(requireContext(), MusicActivity::class.java)
            intent.putExtra("class", "NowPlaying")
            intent.putExtra("songPosition", MusicActivity.songPosition)
            intent.putExtra("songsList", MusicActivity.songsList)
            ContextCompat.startActivity(requireContext(), intent, null)
        }

        return binding.root
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        // Load the image from the URI and create a Bitmap
        return requireActivity().contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: throw IllegalArgumentException("Unable to load image from URI: $uri")
    }

    private fun playMusic() {
        songsListActivity.setAdapter()
        MusicActivity.isPlaying = true
        MusicActivity.musicService!!.mediaPlayer.start()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
        songsListActivity.setAdapter()
    }

    private fun pauseMusic() {
        songsListActivity.setAdapter()
        MusicActivity.isPlaying = false
        MusicActivity.musicService!!.mediaPlayer.pause()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))

    }

    override fun onResume() {
        super.onResume()
        songsListActivity.setAdapter()
        if (MusicActivity.musicService != null) {
            binding.root.visibility = View.VISIBLE
            setLayout()
            if (MusicActivity.isPlaying) binding.playPauseButton.setImageDrawable(
                resources.getDrawable(
                    R.drawable.pause
                )
            )
            else binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
        }
    }

    fun setLayout() {
        val bitmap =
            getBitmapFromUri(MusicActivity.songsList!![MusicActivity.songPosition].albumArtUri!!.toUri())
        Palette.from(bitmap).generate { palette ->
            vibrantColor = palette?.getVibrantColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )!!.toInt()


            var lightVibrantColor = vibrantColor?.let {
                Color.argb(
                    70,
                    Color.red(it),
                    Color.green(it),
                    Color.blue(it)
                )
            }
            val red = Color.red(vibrantColor)
            val green = Color.green(vibrantColor)
            val blue = Color.blue(vibrantColor)

            val isWhite = (red >= 220 && green >= 220 && blue >= 220)

            if(isWhite){
                lightVibrantColor = Color.LTGRAY
                val temp = vibrantColor
                vibrantColor = lightVibrantColor!!.toInt()
                lightVibrantColor = temp
            }

            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(vibrantColor!!, lightVibrantColor!!.toInt())
            )

            gradientDrawable.cornerRadius = 0f
            binding.llNowPlaying.background = gradientDrawable
            songsListActivity.binding.root.background = ColorDrawable(lightVibrantColor)
            songsListActivity.window.statusBarColor = vibrantColor

//            val customDrawable = ContextCompat.getDrawable(requireContext(),(R.drawable.layer_list_bg))
//            customDrawable.let {
//                customDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), lightVibrantColor.toInt()), PorterDuff.Mode.SRC_IN)
//                songsListActivity.binding.searchView.background = it
//            }

            songsListActivity.setAdapter()
        }
        binding.singerName.text = MusicActivity.songsList!![MusicActivity.songPosition].artist
        binding.songName.text = MusicActivity.songsList!![MusicActivity.songPosition].name
        Glide.with(requireContext())
            .load(MusicActivity.songsList!![MusicActivity.songPosition].albumArtUri)
            .into(binding.icon)
    }
}