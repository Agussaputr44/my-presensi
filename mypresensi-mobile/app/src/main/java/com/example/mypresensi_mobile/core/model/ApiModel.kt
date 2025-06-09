package com.example.mypresensi_mobile.core.model

import com.google.gson.annotations.SerializedName

// Model untuk response user
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String
)

// Model untuk response login
data class LoginResponse(
    @SerializedName("access_token") val token: String, // pastikan key sesuai dengan API
    val user: UserResponse
)

// Model untuk response logout
data class LogoutResponse(
    val message: String
)


// Model untuk lokasi
data class LokasiModel(
    val id: Int,
    val name: String
)

// Model untuk request register
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// Model untuk request login
data class LoginRequest(
    val email: String,
    val password: String
)

data class AbsensiModel(
    val user_id: Int,
    val location_id: Int,
    val location_name: String?,
    val type: String,
    val status: String,
    val latitude: Double,
    val longitude: Double,
    val reason: String?,
    val check_in_time: String?,
    val check_out_time: String?,
    val device_name: String,
    val attachment: String?
)

data class AbsensiRequest(
    val user_id: Int,
    val location_id: Int,
    val location_name: String?,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val check_in_time: String?,
    val check_out_time: String?,
    val reason: String?,
    val device_name: String,
    val attachment: String?
)

data class AttendanceResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<AttendanceData>
)

data class AttendanceData(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("location_id") val locationId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("status") val status: String,
    @SerializedName("reason") val reason: String?,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("check_in_time") val checkInTime: String,
    @SerializedName("check_out_time") val checkOutTime: String
)