package com.droidbytes.musicplayer

import java.io.Serializable

data class Songs(
    val name: String = "",
    val filePath: String = "",
    val albumArtUri: String?,
    val artist : String =""
 ) : Serializable


fun nextSong(){
    if (MusicActivity.songPosition == MusicActivity.songsList.size - 1) {
        MusicActivity.songPosition = 0
    } else {
        ++MusicActivity.songPosition
    }

}

fun prevSong(){
    if (MusicActivity.songPosition == 0) {
        MusicActivity.songPosition = MusicActivity.songsList.size - 1
    } else {
        --MusicActivity.songPosition
    }
}