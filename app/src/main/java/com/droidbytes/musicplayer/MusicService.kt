package com.droidbytes.musicplayer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import java.io.IOException
import java.lang.Exception
import com.droidbytes.musicplayer.MusicActivity

class MusicService : Service(), AudioManager.OnAudioFocusChangeListener {

    lateinit var mediaPlayer: MediaPlayer
    private var isPaused = false
    lateinit var audioManager: AudioManager
    private lateinit var runnable: Runnable



    inner class MusicBinder : Binder() {
        fun getService(): MusicService {
          return this@MusicService
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return MusicBinder()
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    fun createMediaPlayer() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
                println("null" + "yes")
            }
            mediaPlayer.reset()
            println("ye h ->" + MusicActivity.songsList!![MusicActivity.songPosition].filePath)
            mediaPlayer.setDataSource(MusicActivity.songsList!![MusicActivity.songPosition].filePath)
            mediaPlayer.prepare()
            MusicActivity.binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
            MusicActivity.binding.seekBar.progress = 0F
            MusicActivity.binding.seekBar.max = MusicActivity.musicService!!.mediaPlayer.duration.toFloat()
            println("play" + "yes")
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
            MusicActivity.binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
//            NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)
            MusicActivity.isPlaying = false
            mediaPlayer.pause()
        }
    }
}
