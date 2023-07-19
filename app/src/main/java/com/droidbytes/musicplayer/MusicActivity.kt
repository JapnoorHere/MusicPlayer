package com.droidbytes.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.droidbytes.musicplayer.databinding.ActivityMusicBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import java.io.IOException

class MusicActivity : AppCompatActivity() {
    lateinit var binding: ActivityMusicBinding
    private var musicBound = false
    private lateinit var playPauseButton: ImageView
    private lateinit var musicDurationText: TextView
    private lateinit var seekBar: SeekBar
    private var handler = Handler()
    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused = false
    private lateinit var mediaPlayerViewModel: MediaPlayerViewModel


    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            updateSeekBar()
            handler.postDelayed(this, 1)
        }
    }

//    private val musicConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            val binder = service as MusicService.MusicBinder
//            musicService = binder.getService()
//            musicBound = true
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            musicBound = false
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer()

        playPauseButton = binding.playPauseButton
        musicDurationText = binding.time
        seekBar = binding.seekbar

        val musicUri = intent.getStringExtra("uri")
        val uri = Uri.parse(musicUri)
//        updatePlayPauseButton()
        mediaPlayerViewModel = ViewModelProvider(this).get(MediaPlayerViewModel::class.java)

        playPauseButton.setOnClickListener {
            if (musicBound) {
                playOrPauseMusic(uri)
                updatePlayPauseButton()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (musicBound) {
                    val progress = seekBar.progress
                    seekTo(progress)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerViewModel.isPlaying = mediaPlayer.isPlaying
    }

    override fun onStart() {
        super.onStart()
        musicBound = true
        println("yes? -> ${mediaPlayerViewModel .isPlaying}")
//        val musicIntent = Intent(this, MusicService::class.java)
//        bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE)
    }

//    override fun onStop() {
//        super.onStop()
//        if (musicBound) {
//            unbindService(musicConnection)
//            musicBound = false
//        }
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isPlaying", mediaPlayer.isPlaying)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isPlaying = savedInstanceState.getBoolean("isPlaying", false)
        // Restore the media player state here
        // For example, you can start playing the music again if it was playing before
        println(isPlaying)
        if (isPlaying) {
            println("yes? -> ${mediaPlayer.isPlaying}")
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }

    fun playOrPauseMusic(uri: Uri) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPaused = true
        } else {
            if (isPaused) {
                mediaPlayer.start()
                isPaused = false
            } else {
                try {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(applicationContext,uri)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    isPaused = false
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(updateSeekBarRunnable, 1000)
    }
    fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

    fun isMusicPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun updateSeekBar() {
        if (musicBound) {
            val currentPosition = getCurrentPosition()
            val duration = getDuration()
            val formattedCurrentPosition = formatTime(currentPosition)
            val formattedDuration = formatTime(duration)
            musicDurationText.text = "$formattedCurrentPosition / $formattedDuration"
            seekBar.max = duration
            seekBar.progress = currentPosition
        }
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updatePlayPauseButton() {
        if (isMusicPlaying()) {
            playPauseButton.setBackgroundDrawable(resources.getDrawable(R.drawable.pause))
        } else {
            playPauseButton.setBackgroundDrawable(resources.getDrawable(R.drawable.play))
        }
    }
}