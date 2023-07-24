package com.droidbytes.musicplayer

import android.net.Uri
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
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            Media._ID,
            Media.TITLE,
            Media.DATA,
            Media.ALBUM_ID,
            Media.ARTIST
        )
        val cursor = contentResolver.query(uri,projection,selection,null,null)
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(Media._ID)
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdIndex = it.getColumnIndexOrThrow(Media.ALBUM_ID)
            val artistIndex = it.getColumnIndexOrThrow(Media.ARTIST)

            while (it.moveToNext()){
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val filePath = it.getString(dataIndex)
                val artist = it.getString(artistIndex)
                val albumId = it.getLong(albumIdIndex)
                val albumArtUri = getAlbumArtUri(albumId)
                val song = Songs(title, filePath,albumArtUri.toString(),artist)
                songsList.add(song)
                println("Song $songsList")
            }
        }
        songsList.sortBy {
            it.name
        }
        songAdapter=SongAdapter(this@SongsListActivity,songsList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this@SongsListActivity)
        binding.recyclerView.adapter=songAdapter
    }
    private fun getAlbumArtUri(albumId: Long): Uri? {
        val uri = Uri.parse("content://media/external/audio/albumart")
        return Uri.withAppendedPath(uri, albumId.toString())
    }
}