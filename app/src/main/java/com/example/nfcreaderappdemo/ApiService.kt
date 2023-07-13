package com.example.nfcreaderappdemo

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("your-api-endpoint")
    fun sendData(@Body request: ApiRequest): Call<ResponseBody>
}

