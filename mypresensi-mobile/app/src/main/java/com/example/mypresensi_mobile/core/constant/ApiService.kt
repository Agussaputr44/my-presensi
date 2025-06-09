package com.example.mypresensi_mobile.core.service

import com.example.mypresensi_mobile.core.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint untuk register
    @POST("v1/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<UserResponse>

    // Endpoint untuk login
    @POST("v1/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    // Endpoint untuk mendapatkan data user
    @GET("v1/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    // Endpoint untuk logout
    @POST("v1/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    @POST("v1/absensi")
    suspend fun createAbsensi(
        @Header("Authorization") token: String,
        @Body absensiRequest: AbsensiRequest
    ): Response<AbsensiModel>

    // Endpoint untuk absensi berdasarkan user ID
    @GET("v1/user/history")
    suspend fun getAbsensiByUserId(
        @Header("Authorization") token: String
    ): Response<AttendanceResponse>

    @Multipart
    @POST("v1/absensi")
    suspend fun createAbsensiWithAttachment(
        @Header("Authorization") token: String,
        @Part("user_id") userId: RequestBody,
        @Part("location_id") locationId: RequestBody,
        @Part("location_name") locationName: RequestBody?,
        @Part("type") type: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("check_in_time") checkInTime: RequestBody?,
        @Part("check_out_time") checkOutTime: RequestBody?,
        @Part("reason") reason: RequestBody?,
        @Part("device_name") deviceName: RequestBody,
        @Part attachment: MultipartBody.Part?
    ): Response<AbsensiModel>
}
