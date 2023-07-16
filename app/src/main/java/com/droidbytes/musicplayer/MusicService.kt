package com.droidbytes.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import java.io.IOException

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused = false

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MusicBinder()
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
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

//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.release()
//    }
}
