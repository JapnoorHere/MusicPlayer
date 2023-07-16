package com.droidbytes.musicplayer

import android.net.Uri

data class Songs(
    val name: String = "",
    val filePath: String = "",
    val albumArtUri: Uri?,
    val artist : String =""
    )
