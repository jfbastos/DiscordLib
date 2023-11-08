package br.com.maximatech.discordlogger.model.enums

import br.com.maximatech.discordlogger.util.Constantes

/**
 * Enumerador do nível do log
 * @property Nivel.DEBUG
 * @property Nivel.INFO
 * @property Nivel.WARNING
 * @property Nivel.ERROR
 * */
enum class Nivel(val color : Int, val tag : String){
    /**
     * Define o nível do log para DEBUG
     * */
    DEBUG(Constantes.COLOR_DEBUG, "DEBUG"),
    /**
     * Define o nível do log para Informação
     * */
    INFO(Constantes.COLOR_INFO, "INFO"),
    /**
     * Define o nível do log para Alerta
     * */
    WARNING(Constantes.COLOR_WARNING, "WARNING"),
    /**
     * Define o nível do log para Erro
     * */
    ERROR(Constantes.COLOR_ERROR, "ERROR");
}