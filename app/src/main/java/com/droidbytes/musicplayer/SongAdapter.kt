package com.droidbytes.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droidbytes.musicplayer.databinding.SongItemBinding
import java.io.File

class SongAdapter(
    private var songsListActivity: SongsListActivity,
    private var songsList: ArrayList<Songs>,
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {


    class ViewHolder(var binding: SongItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SongItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.songName.text = songsList[position].name
        holder.binding.singerName.text = songsList[position].artist

        Glide.with(songsListActivity)
            .load(songsList[position].albumArtUri)
            .into(holder.binding.icon)

        holder.itemView.setOnClickListener {
//            val stopIntent = Intent(MusicService.ACTION_STOP)
//            songsListActivity.sendBroadcast(stopIntent)

//            val intent1 = Intent(songsListActivity, MusicService::class.java)
//            lateinit var musicService : MusicService
//            val musicConnection = object : ServiceConnection {
//                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//                    val binder = service as MusicService.MusicBinder
//                    musicService = binder.getService()
//                    if (musicService.isMusicPlaying()) {
//                        musicService.stopMusic()
//                    }
//                }
//
//                override fun onServiceDisconnected(p0: ComponentName?) {
//                    TODO("Not yet implemented")
//                }
//
//            }
//            songsListActivity.bindService(intent1, musicConnection, Context.BIND_AUTO_CREATE)

            val intent = Intent(songsListActivity, MusicActivity::class.java)
            intent.putExtra("songsList", songsList)
            intent.putExtra("songPosition", position.toString())
            songsListActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }


}




