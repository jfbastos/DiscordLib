package br.com.maximatech.discordlogger

import android.os.Build
import br.com.maximatech.discordlogger.model.DadosUsuario
import br.com.maximatech.discordlogger.model.DiscordLogger
import br.com.maximatech.discordlogger.model.Embed
import br.com.maximatech.discordlogger.model.Field
import br.com.maximatech.discordlogger.model.QueryData
import br.com.maximatech.discordlogger.model.WebHook
import br.com.maximatech.discordlogger.repository.DiscordRepository
import br.com.maximatech.discordlogger.util.Constantes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

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
     * Salva logs de nível DEBUG no channel principal do discord.
     * @param tag define o nome dado o boot. Se vazio será utilizado o valor do parâmetro callingClass; **Se os dois forem vazios não será enviada a mensagem.**
     * @param title define o titulo do corpo da mensagem
     * @param usuario define informações importantes sobre o usuário.
     * @param callingClass define a classe onde o log foi chamado.
     *
     * */
    fun logD(tag : String = "",  title : String ,mensagem : String , usuario: DadosUsuario, callingClass : String = "") = CoroutineScope(Dispatchers.IO).launch{
       repository.sendMessage(DiscordLogger(
           tag = tag.takeIf { it.isNotBlank() } ?: callingClass,
           embeds = listOf(Embed(
               title = title,
               description = "Log debug: $mensagem",
               color = Constantes.COLOR_DEBUG,
               fields = informacoes(usuario)))), getQuery(usuario, title, "DEBUG", callingClass))
   }

    /**
     * Salva logs de nível ERRO no channel principal do discord.
     * @param tag define o nome dado o boot. Se vazio será utilizado o valor do parâmetro callingClass; **Se os dois forem vazios não será enviada a mensagem.**
     * @param title define o titulo do corpo da mensagem
     * @param usuario define informações importantes sobre o usuário.
     * @param callingClass define a classe onde o log foi chamado.
     *
     * */
    fun logE(tag : String = "",  title : String ,mensagem : String , usuario: DadosUsuario,callingClass : String = "") = CoroutineScope(Dispatchers.IO).launch{
        repository.sendMessage(DiscordLogger(
            tag = tag.takeIf { it.isNotBlank() } ?: callingClass,
            embeds = listOf(Embed(
                title = title,
                description = "Log erro at $callingClass: $mensagem",
                color = Constantes.COLOR_ERROR,
                fields = informacoes(usuario)))), getQuery(usuario, title, "ERRO", callingClass))
    }

    /**
     * Salva logs de nível INFO no channel principal do discord.
     * @param tag define o nome dado o boot. Se vazio será utilizado o valor do parâmetro callingClass; **Se os dois forem vazios não será enviada a mensagem.**
     * @param title define o titulo do corpo da mensagem
     * @param usuario define informações importantes sobre o usuário.
     * @param callingClass define a classe onde o log foi chamado.
     *
     * */
    fun logI(tag : String = "",  title : String ,mensagem : String , usuario: DadosUsuario, callingClass : String = "") = CoroutineScope(Dispatchers.IO).launch{
        repository.sendMessage(DiscordLogger(
            tag = tag.takeIf { it.isNotBlank() } ?: callingClass,
            embeds = listOf(Embed(
                title = title,
                description = "Log info: $mensagem",
                color = Constantes.COLOR_INFO,
                fields = informacoes(usuario)))), getQuery(usuario, title, "INFO", callingClass))
    }

    /**
     * Salva logs de nível WARNING no channel principal do discord.
     * @param tag define o nome dado o boot. Se vazio será utilizado o valor do parâmetro callingClass; **Se os dois forem vazios não será enviada a mensagem.**
     * @param title define o titulo do corpo da mensagem
     * @param usuario define informações importantes sobre o usuário.
     * @param callingClass define a classe onde o log foi chamado.
     *
     * */
    fun logW(tag : String = "",  title : String ,mensagem : String , usuario: DadosUsuario, callingClass : String = "") = CoroutineScope(Dispatchers.IO).launch{
        repository.sendMessage(DiscordLogger(
            tag = tag.takeIf { it.isNotBlank() } ?: callingClass,
            embeds = listOf(Embed(
                title = title,
                description = "Log alerta: $mensagem",
                color = Constantes.COLOR_WARNING,
                fields = informacoes(usuario)))), getQuery(usuario, title, "WARNING", callingClass))
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