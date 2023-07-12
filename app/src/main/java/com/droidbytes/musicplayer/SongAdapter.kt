package com.droidbytes.musicplayer

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.droidbytes.musicplayer.databinding.SongItemBinding
import java.io.File

class SongAdapter(private var songsListActivity: SongsListActivity, private var songsList : ArrayList<Songs>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
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
        holder.binding.songName.text=songsList[position].name

        holder.itemView.setOnClickListener {
            val filepath = File(songsList[position].filePath)
            val uri = Uri.fromFile(filepath)
            val intent=Intent(songsListActivity,MusicActivity::class.java)
            intent.putExtra("uri", uri.toString())
            songsListActivity.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }
}




