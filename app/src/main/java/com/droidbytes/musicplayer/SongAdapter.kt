package com.droidbytes.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
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

    var holderGlobal : ViewHolder? = null

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
        holderGlobal = holder

        if (MusicActivity.songPosition.toString() == position.toString() && MusicActivity.isPlaying) {
            println(MusicActivity.songPosition.toString() + " " + position.toString())
            holder.binding.lottieView.visibility = View.VISIBLE
            holder.itemView.background = ColorDrawable(MusicActivity.vibrantColor)
        } else {
            holder.binding.lottieView.visibility = View.INVISIBLE
        }

        Glide.with(songsListActivity)
            .load(songsList[position].albumArtUri)
            .into(holder.binding.icon)
        println(MusicActivity.nowPlayingSongId == songsList[position].id)
        holder.itemView.setOnClickListener {
            val intent = Intent(songsListActivity, MusicActivity::class.java)
            intent.putExtra("songsList", songsList)
            intent.putExtra("songPosition", position)
            if (MusicActivity.nowPlayingSongId == songsList[position].id) {
                intent.putExtra("songPosition", MusicActivity.songPosition)
            } else {
                intent.putExtra("songPosition", position)
            }
            songsListActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

}




