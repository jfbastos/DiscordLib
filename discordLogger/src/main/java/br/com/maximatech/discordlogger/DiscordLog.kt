package br.com.maximatech.discordlogger

import android.os.Build
import br.com.maximatech.discordlogger.model.DadosUsuario
import br.com.maximatech.discordlogger.model.DiscordLogger
import br.com.maximatech.discordlogger.model.Embed
import br.com.maximatech.discordlogger.model.Field
import br.com.maximatech.discordlogger.model.QueryData
import br.com.maximatech.discordlogger.model.WebHook
import br.com.maximatech.discordlogger.model.enums.Nivel
import br.com.maximatech.discordlogger.repository.DiscordRepository
import br.com.maximatech.discordlogger.util.Constantes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.logging.Level

class DiscordLog private constructor(private val isSaveQueryData : Boolean = false) {

    data class Builder(
        var isSaveQueryData: Boolean = false
    ){
        /**
         * Define o webHook do channel principal do discord onde os logs principais (formatados) serão apresentados
         * @param webHookUrl URL do webHook do channel
         * */
        fun webHook(webHookUrl : String) = apply { WebHook.URL = webHookUrl }
        /**
         * Define o webHook do channel query onde os dados concatenados serão salvos para futuras exportações (ex: csv)
         * @param webHookUrl URL do webHook do channel
         * */
        fun queryWebHook(webHookUrl: String) = apply { WebHook.QUERYCHANNEL = webHookUrl }
        /**
         * Define se serão salvos os dados em query para exportação.
         * @param isSaveQueryData Se true, salva no channel definido no método queryWebHook, se false não salva nada.
         * */
        fun isSaveQueryData(isSaveQueryData: Boolean) = apply { this.isSaveQueryData = isSaveQueryData }
        fun build() = DiscordLog(isSaveQueryData)
    }

    private val repository : DiscordRepository by inject(DiscordRepository::class.java)

    /**
     * Salva logs no channel principal do discord.
     * @param nivelDoLog Define o nível do log a ser registrado
     * @param title define o titulo do corpo da mensagem
     * @param usuario define informações importantes sobre o usuário.
     * @param callingClass define a classe onde o log foi chamado.
     * @see Nivel
     *
     * */
    fun log(nivelDoLog : Nivel, title : String ,mensagem : String , usuario: DadosUsuario, callingClass : String) = CoroutineScope(Dispatchers.IO).launch {
        repository.sendMessage(DiscordLogger(
            tag = callingClass,
            embeds = listOf(Embed(
                title = title,
                description = "***Log de ${nivelDoLog.tag}***: $mensagem",
                color = nivelDoLog.color,
                fields = informacoes(usuario)))), getQuery(usuario, title, nivelDoLog.tag, callingClass))
    }

    private fun informacoes(usuario: DadosUsuario) : List<Field>{
        val dadosUsuario = Field(name = "Dados do usuário", "*Nome*: ${usuario.nome}\n*Código*: ${usuario.codigo}\n*Cod. cliente*: ${usuario.codigoCliente}\n*Cliente*: ${usuario.cliente}")
        val phoneInfo = Field(name = "Dados do celular", value = "\n*Android*: ${Build.VERSION.RELEASE}\n*Modelo*: ${Build.MODEL}\n*Versão app*: ${usuario.versaoApp}")
        return listOf(dadosUsuario, phoneInfo)
    }

    private fun getQuery(usuario: DadosUsuario, tag : String,level: String, callingClass : String) : QueryData?{
        return if(isSaveQueryData && WebHook.QUERYCHANNEL.isNotBlank()) {
            QueryData(username = callingClass, content = "$callingClass,$level,$tag,${usuario.nome},${usuario.codigo},${usuario.codigoCliente},${usuario.cliente},${usuario.versaoApp},${Build.VERSION.RELEASE},${Build.MODEL}")
        }else{
            null
        }
    }

}