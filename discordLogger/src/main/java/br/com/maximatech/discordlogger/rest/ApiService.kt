package br.com.maximatech.discordlogger.rest

import br.com.maximatech.discordlogger.model.DiscordLogger
import br.com.maximatech.discordlogger.model.QueryData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    fun sendMessageAtLogger(@Url url : String, @Body message : DiscordLogger): Call<ResponseBody>

    @POST
    fun sendMessageAtQuery(@Url url : String,@Body message : QueryData) : Call<ResponseBody>

}