package com.zistus.aad.data.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    fun network(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://lookup.binlist.net/")
            .client(okhttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun okhttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        Log.d(javaClass.simpleName, "Logging Interceptor $loggingInterceptor")
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
