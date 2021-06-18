package com.example.test.api

import com.example.test.api.info.Auth
import com.example.test.api.info.Info
import com.example.test.api.info.Status
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    @GET("requestSMSCodeClient")
    fun requestSMSCodeClient(
        @Query("phone_number") phoneNumber: Int
    ): Status

    @GET("getInfo")
    fun getInfo(
        @Query("token") token: String
    ): Info

    @POST("authenticateClients")
    fun authenticateClients(
        @Query("phone_number") phoneNumber: Int,
        @Query("password") password: Int
    ): Auth
}