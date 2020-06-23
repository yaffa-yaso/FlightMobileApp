package com.example.flightmobileapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbEndpoints {

    @GET("GET /screenshot")
    fun getImage(): Call<SimulatorImage>

}
