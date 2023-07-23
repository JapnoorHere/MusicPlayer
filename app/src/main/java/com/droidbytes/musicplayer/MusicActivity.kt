package com.droidbytes.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.LoudnessEnhancer
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.droidbytes.musicplayer.databinding.ActivityMusicBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import java.io.IOException

class MusicActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    companion object {
        var musicBound = false
        lateinit var binding: ActivityMusicBinding
        var musicService: MusicService? = null
        lateinit var songsList: ArrayList<Songs>
        private var handler = Handler()
        var isPlaying = false
        var songPosition: Int = 0
    }

//    private val updateSeekBarRunnable = object : Runnable {
//        override fun run() {
//            updateSeekBar()
//            handler.postDelayed(this, 100)
//        }
//    }

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
        songsList = ArrayList()

        songsList = intent.getSerializableExtra("songsList") as ArrayList<Songs>
        val pos = intent.getStringExtra("songPosition").toString()
        songPosition = Integer.parseInt(pos)

//        musicDurationText = binding.time

        binding.singerName.text = songsList[songPosition].artist
        binding.songName.text = songsList[songPosition].name
        Glide.with(this@MusicActivity).load(songsList[songPosition].albumArtUri)
            .into(binding.songIcon)

        println("ethe" + songsList)

//        updatePlayPauseButton()

        binding.playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object :
            me.tankery.lib.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                circularSeekBar: me.tankery.lib.circularseekbar.CircularSeekBar?,
                progress: Float,
                fromUser: Boolean,
            ) {
                if(fromUser){
                    musicService!!.mediaPlayer.seekTo(progress.toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: me.tankery.lib.circularseekbar.CircularSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: me.tankery.lib.circularseekbar.CircularSeekBar?) {

            }
        })
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (musicService == null) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            musicService!!.audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            musicService!!.audioManager.requestAudioFocus(
                musicService,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
        createMediaPlayer()
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }


    private fun createMediaPlayer() {
        try {
            if (musicService?.mediaPlayer == null) { musicService?.mediaPlayer = MediaPlayer() }
            musicService!!.mediaPlayer.reset()
            musicService!!.mediaPlayer.setDataSource(songsList[songPosition].filePath)
            musicService!!.mediaPlayer.prepare()
            musicService!!.mediaPlayer.setOnCompletionListener(this@MusicActivity)
            playMusic()
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        musicBound = true
        val musicIntent = Intent(this, MusicService::class.java)
        bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

//    override fun onStop() {
//        super.onStop()
//        if (musicBound) {
//            unbindService(musicConnection)
//            musicBound = false
//        }
//    }

    private fun playMusic() {
        isPlaying = true
        musicService!!.mediaPlayer.start()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
    }

    private fun pauseMusic() {
        isPlaying = false
        musicService!!.mediaPlayer.pause()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
    }

    fun getCurrentPosition(): Int {
        return musicService!!.mediaPlayer.currentPosition
    }

    fun getDuration(): Int {
        return musicService!!.mediaPlayer.duration
    }

    override fun onResume() {
        super.onResume()
//        handler.postDelayed(updateSeekBarRunnable, 1000)
    }
//    fun seekTo(position: Int) {
//        mediaPlayer.seekTo(position)
//    }

    fun isMusicPlaying(): Boolean {
        return musicService!!.mediaPlayer.isPlaying
    }

    override fun onPause() {
        super.onPause()
//        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun updateSeekBar() {
        if (musicBound) {
            val currentPosition = musicService!!.mediaPlayer.currentPosition
            val duration = musicService!!.mediaPlayer.duration
//            val formattedCurrentPosition = formatTime(currentPosition)
//            val formattedDuration = formatTime(duration)
//            musicDurationText.text = "$formattedCurrentPosition / $formattedDuration"
            binding.seekBar.max = duration.toFloat()
            binding.seekBar.progress = currentPosition.toFloat()
        }
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCompletion(p0: MediaPlayer?) {
        createMediaPlayer()
    }
}
