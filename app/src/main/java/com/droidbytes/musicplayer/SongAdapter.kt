package com.droidbytes.musicplayer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droidbytes.musicplayer.databinding.SongItemBinding
import java.io.InputStream


class SongAdapter(
    private var songsListActivity: SongsListActivity, private var songsList: ArrayList<Songs>,
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    var holderGlobal: ViewHolder? = null
    var originalList : ArrayList<Songs>? = null
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


        if (MusicActivity.songPosition == position&& MusicActivity.isPlaying) {
            println(MusicActivity.songPosition.toString() + " " + position.toString())
            holder.binding.lottieView.visibility = View.VISIBLE
            holder.binding.root.background = ColorDrawable(NowPlayingMusic.vibrantColor)
            holder.binding.songName.setTextColor(Color.LTGRAY)
            holder.binding.singerName.setTextColor(Color.LTGRAY)
        } else {
            holder.binding.lottieView.visibility = View.INVISIBLE
        }
        if(getBitmapFromUri(songsList[position].albumArtUri?.toUri()!!)!=null) {

            Glide.with(songsListActivity)
                .load(songsList[position].albumArtUri)
                .into(holder.binding.icon)
        }
        else{
            val uri = Uri.parse("android.resource://com.droidbytes.musicplayer/drawable/music")
            songsList[position].albumArtUri = uri.toString()

            Glide.with(songsListActivity)
                .load(songsList[position].albumArtUri)
                .into(holder.binding.icon)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(songsListActivity, MusicActivity::class.java)

            if(originalList !=null) {
                intent.putExtra("songsList", originalList)
                intent.putExtra("songPosition", originalList?.indexOf(songsList[position]))
            }
            else{
                intent.putExtra("songsList", songsList)
                intent.putExtra("songPosition", position)
            }
            if (MusicActivity.nowPlayingSongId == songsList[position].id) {
                intent.putExtra("songPosition", MusicActivity.songPosition)
            } else {
                intent.putExtra("songPosition", position)
            }
            songsListActivity.startActivity(intent)
        }

        holder.binding.more.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    fun updateMusicList(searchList : ArrayList<Songs>){
        if(originalList == null) {
            originalList = songsList
        }
        songsList = ArrayList()
        songsList.addAll(searchList)
        notifyDataSetChanged()
    }
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            // Load the image from the URI and create a Bitmap
            songsListActivity.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
//            e.printStackTrace()
            null
        }
    }

}




