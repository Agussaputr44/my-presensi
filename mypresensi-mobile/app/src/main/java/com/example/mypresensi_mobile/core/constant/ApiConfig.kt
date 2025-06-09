package com.example.mypresensi_mobile.core.constant

import com.example.mypresensi_mobile.core.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    // Ganti dengan BASE_URL API Anda
    private const val BASE_URL = "https://c0f9-2404-c0-10b0-00-9b-5a6b.ngrok-free.app/api/"

    // Fungsi untuk membuat instance Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
