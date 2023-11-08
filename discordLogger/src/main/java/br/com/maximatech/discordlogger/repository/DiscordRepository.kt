package br.com.maximatech.discordlogger.repository

import android.util.Log
import br.com.maximatech.discordlogger.model.DiscordLogger
import br.com.maximatech.discordlogger.model.QueryData
import br.com.maximatech.discordlogger.model.WebHook
import br.com.maximatech.discordlogger.rest.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

internal class DiscordRepository(private val apiService: ApiService, private val dispatcher: CoroutineDispatcher) {
    suspend fun sendMessage(discordLogger: DiscordLogger, queryData: QueryData? = null) = withContext(dispatcher) {
        apiService.sendMessageAtLogger(WebHook.URL , discordLogger).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                queryData?.let {
                    apiService.sendMessageAtQuery(WebHook.QUERYCHANNEL,it).enqueue(object : retrofit2.Callback<ResponseBody>{
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {}

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e(this@DiscordRepository::class.java.name, t.stackTraceToString())
                        }
                    })
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(this@DiscordRepository::class.java.name, t.stackTraceToString())
            }
        })
    }
}