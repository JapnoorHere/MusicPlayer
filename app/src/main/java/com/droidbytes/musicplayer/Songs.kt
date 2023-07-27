package com.droidbytes.musicplayer

import java.io.Serializable
import kotlin.system.exitProcess

data class Songs(
    val id: String = "",
    val name: String = "",
    val filePath: String = "",
    val albumArtUri: String?,
    val artist: String = "",
) : Serializable


fun nextSong() {
    if (MusicActivity.songPosition == MusicActivity.songsList.size - 1) {
        MusicActivity.songPosition = 0
    } else {
        ++MusicActivity.songPosition
    }

}

fun prevSong() {
    if (MusicActivity.songPosition == 0) {
        MusicActivity.songPosition = MusicActivity.songsList.size - 1
    } else {
        --MusicActivity.songPosition
    }
}

fun exitApplication() {
    if (MusicActivity.musicService != null) {
        MusicActivity.musicService!!.audioManager.abandonAudioFocus(MusicActivity.musicService)
        MusicActivity.musicService!!.stopForeground(true)
        MusicActivity.musicService!!.mediaPlayer.release()
        MusicActivity.musicService = null
    }
    exitProcess(1)
}