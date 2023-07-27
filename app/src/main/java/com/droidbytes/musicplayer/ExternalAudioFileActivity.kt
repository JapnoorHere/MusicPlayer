package com.droidbytes.musicplayer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ExternalAudioFileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external_audio_file)
        if (intent.action == Intent.ACTION_VIEW && intent.type?.startsWith("audio/") == true) {

            val audioUri: Uri? = intent.data
            if (audioUri != null) {
                println(audioUri)
            }
        } else {
        }
    }
}