package com.droidbytes.musicplayer

import java.io.Serializable

data class Songs(
    val name: String = "",
    val filePath: String = "",
    val albumArtUri: String?,
    val artist : String =""
 ) : Serializable