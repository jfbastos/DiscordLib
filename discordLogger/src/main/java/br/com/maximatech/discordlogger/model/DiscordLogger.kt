package br.com.maximatech.discordlogger.model

import com.google.gson.annotations.SerializedName

data class DiscordLogger(
    val content : String = "",
    @SerializedName("username") val tag : String,
    val tts : Boolean = false,
    val embeds : List<Embed> = mutableListOf()
)
