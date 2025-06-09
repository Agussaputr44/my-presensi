package com.example.mypresensi_mobile.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mypresensi_mobile.core.constant.ApiConfig
import com.example.mypresensi_mobile.core.screen.auth.LoginScreen
import com.example.mypresensi_mobile.core.screen.auth.RegisterScreen
import com.example.mypresensi_mobile.core.screen.Beranda
import com.example.mypresensi_mobile.core.screen.Absensi
import com.example.mypresensi_mobile.core.screen.HistoryScreen
import com.example.mypresensi_mobile.core.screen.TentangKami
import com.example.mypresensi_mobile.core.screen.Lainnya
import com.example.mypresensi_mobile.core.service.ApiService
import com.example.mypresensi_mobile.ui.theme.MyPresensiTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val apiService = ApiConfig.apiService  // Instantiate ApiService here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            MyPresensiTheme {
                val navController = rememberNavController()
                var userLocation by remember { mutableStateOf("Tidak Diketahui") }
                val context = LocalContext.current

                // Periksa apakah ada auth_token di SharedPreferences
                val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                val authToken = sharedPreferences.getString("auth_token", null)
                val startDestination = if (authToken.isNullOrEmpty()) "login" else "beranda"

                // Request Location Permission
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        getCurrentLocation(context) { location ->
                            if (!detectFakeGPS(location)) {
                                userLocation = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                            } else {
                                Toast.makeText(
                                    context,
                                    "Lokasi palsu terdeteksi. Harap matikan aplikasi lokasi palsu.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Izin lokasi diperlukan untuk fitur ini.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                LaunchedEffect(Unit) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        getCurrentLocation(context) { location ->
                            if (!detectFakeGPS(location)) {
                                userLocation = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                            } else {
                                Toast.makeText(
                                    context,
                                    "Lokasi palsu terdeteksi. Harap matikan aplikasi lokasi palsu.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }

                // Setup Navigation
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(navController = navController, apiService = apiService)
                    }
                    composable("register") {
                        RegisterScreen(navController = navController, apiService = apiService)
                    }
                    composable("beranda") {
                        Beranda(navController = navController, userLocation, context)
                    }
                    composable("absensi") {
                        Absensi(navController = navController, context = context, userLocation)
                    }
                    composable("history") {
                        HistoryScreen(navController = navController, context)
                    }
                    composable("tentangKami") {
                        TentangKami(navController = navController)
                    }
                    composable("lainnya") {
                        Lainnya(navController = navController, context = context)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(context: Context, callback: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                callback(location)
            } else {
                Toast.makeText(context, "Tidak dapat mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Gagal mendapatkan lokasi: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun detectFakeGPS(location: Location): Boolean {
        // Check if the location is from a mock provider
        return location.isFromMockProvider
    }
}
