package br.com.maximatech.discordlogger.model

data class Field(
    val name : String,
    val value : String,
    val inline : Boolean = false
)
