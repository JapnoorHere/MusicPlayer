package com.droidbytes.musicplayer

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import java.io.IOException
import java.lang.Exception

class ExternalMusicService : Service(), AudioManager.OnAudioFocusChangeListener {

    lateinit var mediaPlayer: MediaPlayer
    private var isPaused = false
    lateinit var audioManager: AudioManager
    private lateinit var runnable: Runnable


    inner class ExternalMusicBinder : Binder() {
        fun getService(): ExternalMusicService {
            return this@ExternalMusicService
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return ExternalMusicBinder()
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    fun createMediaPlayer() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(ExternalAudioFileActivity.audioFilePath)
            mediaPlayer.prepare()
            ExternalAudioFileActivity.binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
            ExternalAudioFileActivity.binding.seekBar.progress = 0F
            ExternalAudioFileActivity.binding.seekBar.max = ExternalAudioFileActivity.externalMusicService!!.mediaPlayer.duration.toFloat()
        } catch (e: Exception) {
            println(e.toString())
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

//    fun seekBarSetup(){
//        runnable = Runnable {
//            MusicActivity.binding.seekBar.progress = mediaPlayer.currentPosition.toFloat()
//            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
//        }
//        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
//    }

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

    fun isMusicPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }

    fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }


    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            try {
                mediaPlayer.prepare()
                mediaPlayer.seekTo(0)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        mediaPlayer.release()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if(focusChange <= 0){
            //pause music
            ExternalAudioFileActivity.binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
//            NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)
            ExternalAudioFileActivity.isPlaying = false
            mediaPlayer.pause()
        }
    }
}
