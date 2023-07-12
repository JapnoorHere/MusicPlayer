package com.droidbytes.musicplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.MediaController
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.droidbytes.musicplayer.databinding.ActivityMusicBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private lateinit var player: SimpleExoPlayer
    private var duartion: Int = 0
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE)
        } else {
            val uri = intent.getStringExtra("uri")
            player = SimpleExoPlayer.Builder(this@MusicActivity).build()
            player.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_READY && player.playWhenReady) {
                        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.pause))
                    } else {
                        binding.playPauseButton.setImageDrawable(resources.getDrawable(R.drawable.play))
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    duartion = player.duration.toInt() / 1000
                    binding.seekbar.max = duartion
                    binding.time.text = "0:00 / " + gettimeString(duartion)
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int,
                ) {
                    var currentposition = player.currentPosition.toInt() / 1000
                    binding.seekbar.progress = currentposition
                    binding.time.setText(gettimeString(currentposition) + "/" + gettimeString(player.duration.toInt() / 1000))
                }

            })
            println("Uri $uri")
            val mediaItem =
                MediaItem.fromUri(uri?.toUri()!!)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()

            binding.playPauseButton.setOnClickListener {
                player.playWhenReady = !player.playWhenReady
            }
            binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        player.seekTo(progress.toLong() * 1000)
                        binding.time.text = gettimeString(progress) + "/" + gettimeString(duartion)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })

            val handler = Handler(Looper.getMainLooper())
            handler.post(object : Runnable {
                override fun run() {
                    var currentposition = player.currentPosition.toInt() / 1000
                    binding.seekbar.progress = currentposition
                    binding.time.setText(
                        gettimeString(currentposition) + "/" + gettimeString(
                            duartion
                        )
                    )
                    handler.postDelayed(this, 0)                }

            })

            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (Player.STATE_BUFFERING == playbackState) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

    fun gettimeString(duration: Int): String {
        var min = duration / 60
        var sec = duration % 60
        var time = String.format("%02d:%02d", min, sec)
        return time
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.pause()
        player.stop()
        player.release()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
        player.stop()
        player.release()
    }
}
