package com.droidbytes.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.droidbytes.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler();

        handler.postDelayed({
            var intent = Intent(this@MainActivity,SongsListActivity::class.java)
            startActivity(intent)
            finish()
        },1000)


    }
}