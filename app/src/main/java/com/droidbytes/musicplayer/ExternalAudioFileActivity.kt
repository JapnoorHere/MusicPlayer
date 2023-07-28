package com.droidbytes.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.droidbytes.musicplayer.databinding.ActivityExternalAudioFileBinding

class ExternalAudioFileActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {
    companion object {
        lateinit var binding: ActivityExternalAudioFileBinding
        var songName: String = ""
        var singerName: String = ""
        var songIcon: Uri? = null
        var songUri: Uri? = null
        var songDuration: Long = 0
        var audioFilePath: String = ""
        var externalMusicService: ExternalMusicService? = null
        var musicBound: Boolean = false
        private var handler = Handler()
        var isPlaying = false
    }

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            updateSeekBar()
            handler.postDelayed(this, 100)
        }
    }

    private fun updateSeekBar() {
        if (musicBound) {
            val currentPosition = externalMusicService!!.mediaPlayer.currentPosition
            var duration = externalMusicService!!.mediaPlayer.duration
//            val formattedCurrentPosition = formatTime(currentPosition)
//            val formattedDuration = formatTime(duration)
//            musicDurationText.text = "$formattedCurrentPosition / $formattedDuration"
            binding.seekBar.max = duration.toFloat()
            binding.seekBar.progress = currentPosition.toFloat()
        }
    }


    private val musicConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (externalMusicService == null) {
                val externalBinder = service as ExternalMusicService.ExternalMusicBinder
                externalMusicService = externalBinder.getService()
                externalMusicService!!.audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                externalMusicService!!.audioManager.requestAudioFocus(
                    externalMusicService,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
            createMediaPlayer()
//            musicService!!.seekBarSetup()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicBound = false
            externalMusicService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExternalAudioFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAudioUri()
        println("f" + getAudioUri())
        if (songUri == null) {
            Toast.makeText(
                this@ExternalAudioFileActivity,
                "Cant Play this Audio",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            bindService()
            getSongDetails()
            setMusicLayout()
            binding.playPauseButton.setOnClickListener {
                if (isPlaying) {
                    pauseMusic()
                } else {
                    playMusic()
                }
            }
        }
        binding.seekBar.setOnSeekBarChangeListener(object :
            me.tankery.lib.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                circularSeekBar: me.tankery.lib.circularseekbar.CircularSeekBar?,
                progress: Float,
                fromUser: Boolean,
            ) {
                if (fromUser) {
                    externalMusicService!!.mediaPlayer.seekTo(progress.toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: me.tankery.lib.circularseekbar.CircularSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: me.tankery.lib.circularseekbar.CircularSeekBar?) {
                externalMusicService!!.mediaPlayer.seekTo(seekBar!!.progress.toInt())
            }
        })
    }

    private fun getAudioUri() {
        if (intent.action == Intent.ACTION_VIEW && intent.type?.startsWith("audio/") == true) {
            val uri: Uri? = intent.data
            if (uri != null) {
                songUri = uri
            }
        }
    }

    private fun setMusicLayout() {
        Glide.with(this).load(songIcon.toString()).into(binding.songIcon)
        binding.singerName.text = singerName
        binding.songName.text = songName
        println("icon " + songIcon)
    }

    private fun getSongDetails() {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.ALBUM_ID)
            cursor =
                this.contentResolver.query(songUri.toString().toUri(), projection, null, null, null)
            println(songUri.toString().toUri())
            val dataColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val songColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val singerColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumId = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            cursor!!.moveToFirst()
            audioFilePath = dataColumn?.let { cursor.getString(it) }.toString()
            songDuration = durationColumn?.let { cursor.getLong(it) }!!
            songName = songColumn?.let { cursor.getString(it) }!!
            singerName = singerColumn?.let { cursor.getString(it) }!!
            songIcon = getAlbumArtUri(albumId!!.toLong())
        } finally {
            cursor?.close()
        }
    }

    private fun createMediaPlayer() {
        try {
            if (externalMusicService!!.mediaPlayer == null) {
                externalMusicService!!.mediaPlayer = MediaPlayer()
            }
            externalMusicService!!.mediaPlayer.reset()
            externalMusicService!!.mediaPlayer.setDataSource(audioFilePath)
            externalMusicService!!.mediaPlayer.prepare()
            binding.seekBar.progress = 0F
            binding.seekBar.max = externalMusicService!!.mediaPlayer.duration.toFloat()
            externalMusicService!!.mediaPlayer.setOnCompletionListener(this@ExternalAudioFileActivity)
            playMusic()
        } catch (e: Exception) {
            println(e.toString())
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun playMusic() {
        isPlaying = true
        externalMusicService!!.mediaPlayer.start()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
    }

    private fun pauseMusic() {
        isPlaying = false
        externalMusicService!!.mediaPlayer.pause()
        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(updateSeekBarRunnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun bindService() {
        musicBound = true
        val musicIntent = Intent(this, ExternalMusicService::class.java)
        bindService(musicIntent, musicConnection, BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        val uri = Uri.parse("content://media/external/audio/albumart")
        return Uri.withAppendedPath(uri, albumId.toString())
    }

    override fun onCompletion(p0: MediaPlayer?) {
        finish()
    }

}