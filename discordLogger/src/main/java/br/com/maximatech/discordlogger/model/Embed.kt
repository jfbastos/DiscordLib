package br.com.maximatech.discordlogger.model

data class Embed(
    val title : String,
    val description : String,
    val color : Int,
    val fields : List<Field>
)