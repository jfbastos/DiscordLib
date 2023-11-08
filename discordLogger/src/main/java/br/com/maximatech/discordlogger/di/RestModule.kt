package br.com.maximatech.discordlogger.di

import br.com.maximatech.discordlogger.repository.DiscordRepository
import br.com.maximatech.discordlogger.rest.ApiService
import br.com.maximatech.discordlogger.util.Constantes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {
    single { provideRetrofit() }
    factory { provideApi(get()) }
}


val dispatcherModule = module {
    single(named("IODispacher")) { Dispatchers.IO }
}

fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build()
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl(Constantes.BASE_URL).addConverterFactory(GsonConverterFactory.create(gsonConvertFactory())).client(okHttpClient()).build()
}

fun gsonConvertFactory() : Gson? {
    return GsonBuilder().setLenient().create()
}

fun provideApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


val repositoryModule = module {
    single {DiscordRepository(get(), get(named("IODispacher")))}
}