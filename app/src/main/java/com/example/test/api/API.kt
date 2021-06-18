package com.example.test.api

import com.example.test.api.info.Auth
import com.example.test.api.info.Info
import com.example.test.api.info.Status
import retrofit2.Call
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    @GET("requestSMSCodeClient")
    fun requestSMSCodeClient(
        @Query("phone_number") phoneNumber: String
    ): Call<Status>

    @GET("getInfo")
    fun getInfo(
        @Query("token") token: String
    ): Call<Info>

    @POST("authenticateClients")
    fun authenticateClients(
        @Query("phone_number") phoneNumber: Int,
        @Query("password") password: Int
    ): Call<Auth>

    companion object {
        val api by lazy { retrofit.create<API>() }
    }
}