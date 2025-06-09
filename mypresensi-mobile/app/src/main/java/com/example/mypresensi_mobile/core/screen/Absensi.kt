package com.example.mypresensi_mobile.core.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mypresensi_mobile.core.constant.ApiConfig
import com.example.mypresensi_mobile.core.model.AbsensiModel
import com.example.mypresensi_mobile.core.model.AbsensiRequest
import com.example.mypresensi_mobile.core.provider.SaveStorage
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission", "CommitPrefEdits")
@Composable
fun Absensi(navController: NavController, context: Context, userLocation: String) {
    val saveStorage = remember { SaveStorage() }
    var currentTime by remember { mutableStateOf("") }
    var currentLocation by remember { mutableStateOf("[Mengambil lokasi...]") }
    var masukTime by remember { mutableStateOf("") }
    var keluarTime by remember { mutableStateOf("") }
    var isMasukClicked by remember { mutableStateOf(false) }
    var isModalVisible by remember { mutableStateOf(false) }
    var modalMessage by remember { mutableStateOf("") }
    var isFakeGPSDetected by remember { mutableStateOf(false) }
    var isLocationDetected by remember { mutableStateOf(false) }
    var showLocationAlert by remember { mutableStateOf(false) }
    var showMasukAlert by remember { mutableStateOf(false) }
    var hasSubmittedToday by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val primaryColor = Color(0xFFFCBA03)
    val cardBackgroundColor = Color(0xFFF5F5F5)

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = LocationRequest.create().apply {
        interval = 5000
        fastestInterval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    // Load stored masukTime, keluarTime, isMasukClicked, and last submission date from SharedPreferences
    LaunchedEffect(Unit) {
        masukTime = saveStorage.getData(context, SaveStorage.KEY_MASUK_TIME) ?: ""
        keluarTime = saveStorage.getData(context, SaveStorage.KEY_KELUAR_TIME) ?: ""
        isMasukClicked = saveStorage.getData(context, SaveStorage.KEY_IS_MASUK_CLICKED)?.toBoolean() ?: false
        val lastSubmissionDate = saveStorage.getData(context, SaveStorage.KEY_LAST_SUBMISSION_DATE)
        val currentDate = LocalDate.now().toString()
        hasSubmittedToday = lastSubmissionDate == currentDate
    }

    DisposableEffect(Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    if (detectFakeGPS(location)) {
                        isFakeGPSDetected = true
                        isLocationDetected = false
                        currentLocation = "[Lokasi terdeteksi sebagai fake GPS]"
                    } else {
                        isFakeGPSDetected = false
                        isLocationDetected = true
                        currentLocation = getLocationName(context, location)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            delay(1000L)
        }
    }

    LaunchedEffect(Unit) {
        delay(30000L)
        if (!isLocationDetected) {
            showLocationAlert = true
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Absensi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Rounded.ArrowBack, "Kembali")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Rounded.Info,
                            contentDescription = "Time",
                            tint = primaryColor,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currentTime,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id"))
                            ),
                            fontSize = 18.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Rounded.LocationOn,
                            contentDescription = "Location",
                            tint = primaryColor,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "LOKASI KAMU",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentLocation,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButtonAbsen(
                        label = "Masuk",
                        isEnabled = !isFakeGPSDetected && isLocationDetected && !isMasukClicked && !hasSubmittedToday,
                        isPrimary = !isMasukClicked,
                        primaryColor = primaryColor,
                        onClickAction = {
                            masukTime = currentTime
                            isMasukClicked = true
                            showMasukAlert = true
                            coroutineScope.launch {
                                saveStorage.saveData(context, SaveStorage.KEY_MASUK_TIME, masukTime)
                                saveStorage.saveData(context, SaveStorage.KEY_IS_MASUK_CLICKED, "true")
                            }
                        }
                    )

                    ActionButtonAbsen(
                        label = "Keluar",
                        isEnabled = isMasukClicked && !isFakeGPSDetected && isLocationDetected && !hasSubmittedToday,
                        isPrimary = isMasukClicked,
                        primaryColor = primaryColor,
                        onClickAction = {
                            keluarTime = currentTime
//                            isMasukClicked = false
                            coroutineScope.launch {
                                saveStorage.saveData(context, SaveStorage.KEY_KELUAR_TIME, keluarTime)
                            }
                        }
                    )

                    ActionButtonAbsen(
                        label = "Lainnya",
                        isEnabled = !isFakeGPSDetected && isLocationDetected,
                        isPrimary = true,
                        primaryColor = Color(0xFF2196F3),
                        onClickAction = { navController.navigate("lainnya")}
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Rounded.DateRange,
                                contentDescription = "Attendance",
                                tint = primaryColor,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Rekap Absensi Hari Ini",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AttendanceTimeField(
                            label = "Jam Masuk",
                            value = masukTime,
                            primaryColor = primaryColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AttendanceTimeField(
                            label = "Jam Keluar",
                            value = keluarTime,
                            primaryColor = primaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            saveStorage.saveData(context, SaveStorage.KEY_MASUK_TIME, masukTime)
                            saveStorage.saveData(context, SaveStorage.KEY_KELUAR_TIME, keluarTime)
                            val success = submitAbsensi(context, masukTime, keluarTime)
                            if (success) {
                                modalMessage = "Absensi berhasil!"
                                val currentDate = LocalDate.now().toString()
                                hasSubmittedToday = true
                                saveStorage.saveData(context, SaveStorage.KEY_LAST_SUBMISSION_DATE, currentDate)
                                masukTime = ""
                                keluarTime = ""
                                saveStorage.saveData(context, SaveStorage.KEY_MASUK_TIME, masukTime)
                                saveStorage.saveData(context, SaveStorage.KEY_KELUAR_TIME, keluarTime)
                                saveStorage.saveData(context, SaveStorage.KEY_IS_MASUK_CLICKED, "false")
                            } else {
                                modalMessage = "Gagal melakukan absensi."
                            }
                            isModalVisible = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(
                        text = "KIRIM PRESENSI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isModalVisible) {
                    AlertDialog(
                        onDismissRequest = { isModalVisible = false },
                        title = { Text("Informasi") },
                        text = { Text(modalMessage, fontSize = 16.sp) },
                        confirmButton = {
                            Button(
                                onClick = { isModalVisible = false },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (showMasukAlert) {
                    AlertDialog(
                        onDismissRequest = { showMasukAlert = false },
                        title = { Text("Informasi") },
                        text = { Text("Selamat bekerja, jangan lupa untuk mengisi absen keluar.", fontSize = 16.sp) },
                        confirmButton = {
                            Button(
                                onClick = { showMasukAlert = false },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (isFakeGPSDetected) {
                    AlertDialog(
                        onDismissRequest = { /* Do nothing */ },
                        title = { Text("Peringatan") },
                        text = {
                            Text(
                                "Anda menggunakan fake GPS. Absensi tidak dapat dilakukan.",
                                color = Color.Red
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = { isFakeGPSDetected = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (showLocationAlert) {
                    AlertDialog(
                        onDismissRequest = { showLocationAlert = false },
                        title = { Text("Peringatan") },
                        text = {
                            Text(
                                "Harap nyalakan layanan lokasi Anda.",
                                color = Color.Red
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = { showLocationAlert = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}

suspend fun submitAbsensi(context: Context, checkInTime: String?, checkOutTime: String?): Boolean {
    // Check if location permissions are granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        println("Location permissions are not granted")
        return false
    }

    // Get user_id, auth_token, and device_name from SharedPreferences
    val userId = getUserId(context)
    val authToken = getAuthToken(context)
    val deviceName = getDeviceName() // Retrieve device name
    if (userId == -1 || authToken == null || deviceName == null) {
        println("User ID, Auth Token, or Device Name not found")
        return false
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return try {
        val location = fusedLocationClient.lastLocation.await()
        location?.let {
            // Get location name using Geocoder
            val locationName = getLocationName(context, location)

            val absensiRequest = AbsensiRequest(
                user_id = userId,
                location_id = 1,  // Replace with actual location ID
                location_name = locationName, // Add the location name here
                type = "hadir",
                latitude = it.latitude,
                longitude = it.longitude,
                check_in_time = checkInTime,
                check_out_time = checkOutTime,
                reason = null,
                device_name = deviceName,
                attachment = null
            )

            val response: Response<AbsensiModel> = ApiConfig.apiService.createAbsensi("Bearer $authToken", absensiRequest)
            if (response.isSuccessful) {
                println("Absensi created successfully")
                return true
            } else {
                println("Failed to create absensi: ${response.errorBody()?.string()}")
                return false
            }
        } ?: return false
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

fun getDeviceName(): String {
    val manufacturer = android.os.Build.MANUFACTURER
    val model = android.os.Build.MODEL
    return if (model.startsWith(manufacturer)) {
        model.capitalize()
    } else {
        "${manufacturer.capitalize()} $model"
    }
}

@SuppressLint("MissingPermission")
fun getLocationName(context: Context, location: Location): String {
    val geocoder = Geocoder(context, Locale("id"))
    return try {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            // Membuat alamat yang lebih lengkap
            buildString {
                // Tambahkan nama jalan jika ada
                if (!address.thoroughfare.isNullOrEmpty()) {
                    append(address.thoroughfare)
                }
                // Tambahkan subLocality (kelurahan/desa) jika ada
                if (!address.subLocality.isNullOrEmpty()) {
                    if (length > 0) append(", ")
                    append(address.subLocality)
                }
                // Tambahkan locality (kota/kabupaten)
                if (!address.locality.isNullOrEmpty()) {
                    if (length > 0) append(", ")
                    append(address.locality)
                }
                // Tambahkan provinsi
                if (!address.adminArea.isNullOrEmpty()) {
                    if (length > 0) append(", ")
                    append(address.adminArea)
                }
                // Jika string kosong, gunakan fallback
                if (isEmpty()) {
                    append("[Alamat tidak tersedia]")
                }
            }
        } else {
            "[Alamat tidak tersedia]"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "[Alamat tidak tersedia]"
    }
}

fun detectFakeGPS(location: Location): Boolean {
    // Check if the location is from a mock provider
    return location.isFromMockProvider
}

@Composable
fun AttendanceTimeField(label: String, value: String, primaryColor: Color) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = primaryColor,
                disabledTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun ActionButtonAbsen(
    label: String,
    isEnabled: Boolean,
    isPrimary: Boolean,
    primaryColor: Color,
    onClickAction: () -> Unit
) {
    Button(
        onClick = onClickAction,
        modifier = Modifier
            .width(110.dp)
            .height(45.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) primaryColor else Color.Gray,
            disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getUserId(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("user_id", -1)
}

fun getAuthToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("auth_token", null)
}

@Preview(showBackground = true)
@Composable
fun AbsensiPreview() {
    val context = LocalContext.current
    Absensi(navController = rememberNavController(), context = context, "")
}
