package com.droidbytes.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidbytes.musicplayer.databinding.ActivitySongsListBinding

class SongsListActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongsListBinding
    private lateinit var songAdapter: SongAdapter
    private lateinit var songsList : ArrayList<Songs>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songsList=ArrayList()
        songAdapter=SongAdapter(this@SongsListActivity,songsList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this@SongsListActivity)
        binding.recyclerView.adapter=songAdapter

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.TITLE , MediaStore.Audio.Media.DATA)

        val cursor = contentResolver.query(uri,projection,null,null,null)
        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()){
                val title = it.getString(titleIndex)
                val filePath = it.getString(dataIndex)
                val song = Songs(title, filePath)
                songsList.add(song)
                println("Song $songsList")
            }
        }
        songAdapter=SongAdapter(this@SongsListActivity,songsList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this@SongsListActivity)
        binding.recyclerView.adapter=songAdapter
    }
}