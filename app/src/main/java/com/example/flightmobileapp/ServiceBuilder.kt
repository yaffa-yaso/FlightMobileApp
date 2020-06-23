package com.example.flightmobileapp

import android.view.View
import android.widget.Button
import android.widget.EditText
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    fun<T> buildService(service: Class<T>, url: String): T{
        var retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(service)
    }
}