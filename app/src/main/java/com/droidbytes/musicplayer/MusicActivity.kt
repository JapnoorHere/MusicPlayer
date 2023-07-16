package com.droidbytes.musicplayer

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
import com.droidbytes.musicplayer.databinding.ActivityMusicBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class MusicActivity : AppCompatActivity() {
    lateinit var binding: ActivityMusicBinding
    private lateinit var musicService: MusicService
    private var musicBound = false
    private lateinit var playPauseButton: ImageView
    private lateinit var musicDurationText: TextView
    private lateinit var seekBar: SeekBar
    private var handler = Handler()


    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            updateSeekBar()
            handler.postDelayed(this, 1000)
        }
    }

    private val musicConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            musicBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playPauseButton = binding.playPauseButton
        musicDurationText = binding.time
        seekBar = binding.seekbar

        val musicUri = intent.getStringExtra("uri")
        val uri = Uri.parse(musicUri)

        playPauseButton.setOnClickListener {
            if (musicBound) {
                musicService.playOrPauseMusic(uri)
                updatePlayPauseButton()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (musicBound) {
                    val progress = seekBar.progress
                    musicService.seekTo(progress)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val musicIntent = Intent(this, MusicService::class.java)
        bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE)
    }

//    override fun onStop() {
//        super.onStop()
//        if (musicBound) {
//            unbindService(musicConnection)
//            musicBound = false
//        }
//    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(updateSeekBarRunnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun updateSeekBar() {
        if (musicBound) {
            val currentPosition = musicService.getCurrentPosition()
            val duration = musicService.getDuration()
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

    private fun updatePlayPauseButton() {
        if (musicService.isMusicPlaying()) {
            playPauseButton.setBackgroundResource(R.drawable.pause)
        } else {
            playPauseButton.setBackgroundResource(R.drawable.play)
        }
    }
}