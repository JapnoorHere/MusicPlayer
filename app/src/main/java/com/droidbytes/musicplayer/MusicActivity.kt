package com.droidbytes.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.droidbytes.musicplayer.databinding.ActivityMusicBinding
import androidx.palette.graphics.Palette
import java.io.FileNotFoundException


class MusicActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {

    companion object {
        var musicBound = false
        lateinit var binding: ActivityMusicBinding
        var musicService: MusicService? = null
        var songsList: ArrayList<Songs>? = null
        private var handler = Handler()
        var isPlaying = false
        var songPosition: Int = 0
        var nowPlayingSongId: String = ""
        var vibrantColor : Int = 0
    }

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            updateSeekBar()
            handler.postDelayed(this, 100)
        }
    }

    private val musicConnection = object : ServiceConnection {
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
//            musicService!!.seekBarSetup()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicBound = false
            musicService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songsList = ArrayList()

        songPosition = intent.getIntExtra("songPosition", 0)
        songsList = intent.getSerializableExtra("songsList") as ArrayList<Songs>
        setMusicLayout()

        if (intent.getStringExtra("class").equals("NowPlaying")) {
            binding.seekBar.progress = musicService!!.mediaPlayer.currentPosition.toFloat()
            binding.seekBar.max = musicService!!.mediaPlayer.duration.toFloat()
            println(binding.seekBar.progress.toInt())
            if (isPlaying) binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
            else binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
        } else {
            bindService()
        }
//        updatePlayPauseButton()

        binding.nextButton.setOnClickListener {
            nextSong()
            createMediaPlayer()
            setMusicLayout()
        }

        binding.prevButton.setOnClickListener {
            prevSong()
            createMediaPlayer()
            setMusicLayout()
        }

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
                if (fromUser) {
                    musicService!!.mediaPlayer.seekTo(progress.toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: me.tankery.lib.circularseekbar.CircularSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: me.tankery.lib.circularseekbar.CircularSeekBar?) {
                musicService!!.mediaPlayer.seekTo(seekBar!!.progress.toInt())
            }
        })
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        // Load the image from the URI and create a Bitmap
        try {
            return contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            } ?: throw IllegalArgumentException("Unable to load image from URI: $uri")
        } catch (e: FileNotFoundException) {
            // Handle the FileNotFoundException here
            // For example, show an error message to the user
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            finish()
            e.printStackTrace();
        } catch (e: java.lang.IllegalArgumentException) {
            // Handle the IllegalArgumentException here
            // For example, show an error message to the user
            Toast.makeText(this, e.localizedMessage.toString(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return null
    }


    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) {
                musicService!!.mediaPlayer = MediaPlayer()
            }
            musicService!!.mediaPlayer.reset()
            musicService!!.mediaPlayer.setDataSource(songsList!![songPosition].filePath)
            musicService!!.mediaPlayer.prepare()
            binding.seekBar.progress = 0F
            binding.seekBar.max = musicService!!.mediaPlayer.duration.toFloat()
            musicService!!.mediaPlayer.setOnCompletionListener(this@MusicActivity)
            playMusic()
            nowPlayingSongId = songsList!![songPosition].id
        } catch (e: Exception) {
            println(e.toString())
            println(e.localizedMessage?.toString())
            Toast.makeText(this, e.localizedMessage.toString(), Toast.LENGTH_LONG).show()
        }
    }

        fun setMusicLayout() {
        val bitmap = getBitmapFromUri(songsList!![songPosition].albumArtUri!!.toUri())
        if(bitmap!=null) {
            Palette.from(bitmap).generate { palette ->
                vibrantColor =
                    palette?.getVibrantColor(ContextCompat.getColor(this, R.color.white))!!
                var lightVibrantColor = vibrantColor.let {
                    Color.argb(
                        50,
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
                    vibrantColor = lightVibrantColor.toInt()
                    lightVibrantColor = temp
                }




                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(vibrantColor, lightVibrantColor.toInt())
                )


                gradientDrawable.cornerRadius = 0f
                binding.root.background = gradientDrawable
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = vibrantColor
                }
            }
        }
        binding.singerName.text = songsList!![songPosition].artist
        binding.songName.text = songsList!![songPosition].name
        println(songsList!![songPosition].albumArtUri)
        Glide.with(this@MusicActivity).load(songsList!![songPosition].albumArtUri)
            .into(binding.songIcon)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (songsList!![songPosition].id == "Unknown" && !isPlaying) exitApplication()
    }

    fun bindService() {
        musicBound = true
        val musicIntent = Intent(this, MusicService::class.java)
        bindService(musicIntent, musicConnection, BIND_AUTO_CREATE)
        startService(intent)
    }

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
        handler.postDelayed(updateSeekBarRunnable, 1000)
    }
//    fun seekTo(position: Int) {
//        mediaPlayer.seekTo(position)
//    }

    fun isMusicPlaying(): Boolean {
        return musicService!!.mediaPlayer.isPlaying
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBarRunnable)
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
        nextSong()
        createMediaPlayer()
        setMusicLayout()
    }


}
