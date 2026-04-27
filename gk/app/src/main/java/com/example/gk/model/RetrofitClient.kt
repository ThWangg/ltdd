package com.example.gk.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //máy ảo 10.0.2.2
    //ipconfig nếu máy ngoài
    private const val BASE_URL = "http://10.0.2.2/orderapi/"

    private val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofitInstance.create(ApiService::class.java)
}
