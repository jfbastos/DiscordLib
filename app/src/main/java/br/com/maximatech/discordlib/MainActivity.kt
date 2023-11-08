package br.com.maximatech.discordlib

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.com.maximatech.discordlogger.DiscordLog
import br.com.maximatech.discordlogger.model.DadosUsuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketException

class MainActivity : AppCompatActivity() {

    private val discordLog = DiscordLog.Builder()
        .isSaveQueryData(true)
        .webHook("1171180743305728000/rsv0-Bolp4-l8QT2bB9sHoWAL4rEfJqMC_aOwFZpApe48IHBD2HgFoAuAg4GKc6Q2O2Z")
        .queryWebHook("1171434738205065280/-aQmoedtVzF1ruyDonZiHH4fq6nB02tgB0lkV-wPe-uqdM-pRGSo29cerv5_iSJE86O_")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.findViewById<Button>(R.id.send_message).setOnClickListener {
            sendTestMessages()
        }
    }

    private fun sendTestMessages() = CoroutineScope(Dispatchers.IO).launch {
        for(x in 1..5){
            delay(2000)
            mockLog(x)
        }
    }

    private fun mockedUser(index : Int) : DadosUsuario{
        val users = listOf(
            DadosUsuario(
                "Bruno", index.toString().repeat(4),
                index.plus(2).toString().repeat(4),
                "Doces balas",
                "2.23.11.0${index.mod(2)}"),
            DadosUsuario(
                "Pedro", index.toString().repeat(4),
                index.plus(2).toString().repeat(4),
                "HBO Max",
                "2.23.11.0${index.mod(2)}"),
            DadosUsuario(
                "Karol", index.toString().repeat(4),
                index.plus(2).toString().repeat(4),
                "Doces balas",
                "2.23.11.0${index.mod(2)}"),
            DadosUsuario(
                "Bruno", index.toString().repeat(4),
                index.plus(2).toString().repeat(4),
                "Doces balas",
                "2.23.11.0${index.mod(2)}"),
            DadosUsuario(
                "Jason", index.toString().repeat(4),
                index.plus(2).toString().repeat(4),
                "Sexta 13 fantasias",
                "2.23.11.0${index.mod(2)}"),
        )

        return users[index]
    }

    private fun mockLog(index: Int) {
        when (index) {
            1 -> discordLog.logI(
                    tag = "",
                    title = "Checkout por raio",
                    mensagem ="Usuário com checkout por raio habilitado",
                    usuario = mockedUser(index),
                    callingClass = "TrackingHelper")
            2 -> discordLog.logI(
                    tag = "",
                    title = "Checkout por raio",
                    mensagem ="Usuário com checkout por raio habilitado",
                    usuario = mockedUser(index),
                    callingClass = "TrackinHelper")
            3 -> discordLog.logW(
                    tag = "",
                    title = "Disco cheio",
                    mensagem ="Disco do usuário está com 98% de uso",
                    usuario = mockedUser(index),
                    callingClass = "UtilDispositivo")
            4 -> discordLog.logE(
                    tag = "",
                    title = "Envio visita wrapper",
                    mensagem ="Não foi possível enviar o wrapper. \n\n ${SocketException("No such address").stackTraceToString()}",
                    usuario = mockedUser(index),
                    callingClass = "EnvioVisitaJob")
        }
    }
}
