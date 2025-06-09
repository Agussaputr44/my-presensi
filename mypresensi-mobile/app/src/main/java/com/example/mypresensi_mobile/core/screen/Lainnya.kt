package com.example.mypresensi_mobile.core.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lainnya(navController: NavController, context: Context) {
    var currentTime by remember { mutableStateOf("") }
    var currentLocation by remember { mutableStateOf("[Mengambil lokasi...]") }
    var selectedReason by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isModalVisible by remember { mutableStateOf(false) }
    var modalMessage by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }
    var showMasukAlert by remember { mutableStateOf(false) }
    var hasSubmittedToday by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val coroutineScope = rememberCoroutineScope()

    val primaryColor = Color(0xFFFCBA03)
    val cardBackgroundColor = Color(0xFFF5F5F5)

    // Load stored masukTime, keluarTime, isMasukClicked, and last submission date from SharedPreferences
    LaunchedEffect(Unit) {
        val saveStorage = SaveStorage()
        val lastSubmissionDate = saveStorage.getData(context, SaveStorage.KEY_LAST_SUBMISSION_DATE)
        val currentDate = LocalDate.now().toString()
        hasSubmittedToday = lastSubmissionDate == currentDate
    }

    // LaunchedEffect for time updates
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            delay(1000L)
        }
    }

    // LaunchedEffect for location updates
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fetchLocation(context, fusedLocationClient) { locationName ->
                currentLocation = locationName ?: "[Lokasi tidak tersedia]"
            }
        }
    }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
        fileName = uri?.lastPathSegment ?: ""
    }

    // Main UI
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top App Bar
                TopAppBar(
                    title = {
                        Text(
                            "Lainnya",
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

                // Time and Date Card
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

                // Location Card
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

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton(
                        label = "IZIN",
                        isPrimary = !isButtonClicked,
                        primaryColor = primaryColor,
                        onClickAction = {
                            selectedReason = "IZIN"
                            isButtonClicked = true
                        }
                    )
                    ActionButton(
                        label = "SAKIT",
                        isPrimary = !isButtonClicked,
                        primaryColor = primaryColor,
                        onClickAction = {
                            selectedReason = "SAKIT"
                            isButtonClicked = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Input
                if (selectedReason.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Keterangan: $selectedReason",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BasicTextField(
                                value = description,
                                onValueChange = { description = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(8.dp),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp),
                                        contentAlignment = Alignment.TopStart
                                    ) {
                                        if (description.isEmpty()) {
                                            Text(
                                                text = "Masukkan deskripsi...",
                                                fontSize = 16.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Attachment Button
                Button(
                    onClick = {
                        filePickerLauncher.launch("*/*")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFCBA03))
                ) {
                    Text(
                        text = "TAMBAH LAMPIRAN",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (fileName.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "File terpilih: $fileName",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        if (hasSubmittedToday) {
                            modalMessage = "Anda hanya bisa mengirimkan izin atau sakit sekali dalam sehari."
                            isModalVisible = true
                        } else {
                            coroutineScope.launch {
                                val success = submitReason(context, selectedReason, description, selectedFileUri)
                                if (success) {
                                    val saveStorage = SaveStorage()
                                    saveStorage.saveData(context, SaveStorage.KEY_LAST_SUBMISSION_DATE, LocalDate.now().toString())
                                    modalMessage = "Absensi berhasil!"
                                    isModalVisible = true
                                    // Reset values
                                    selectedReason = ""
                                    description = ""
                                    isButtonClicked = false
                                    fileName = ""
                                    selectedFileUri = null
                                } else {
                                    modalMessage = "Gagal melakukan absensi."
                                    isModalVisible = true
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFCBA03))
                ) {
                    Text(
                        text = "KIRIM PRESENSI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Modal Dialog for success/failure message
                if (isModalVisible) {
                    AlertDialog(
                        onDismissRequest = { isModalVisible = false },
                        confirmButton = {
                            Button(
                                onClick = { isModalVisible = false },
                                colors = ButtonDefaults.buttonColors(primaryColor)
                            ) {
                                Text("OK")
                            }
                        },
                        title = { Text("Informasi") },
                        text = { Text(modalMessage) }
                    )
                }
            }
        }
    }
}

// Helper function to get the last known location and its name
@SuppressLint("MissingPermission")
suspend fun fetchLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (String?) -> Unit
) {
    try {
        val location = fusedLocationClient.lastLocation.await()
        location?.let {
            val locationName = getLocationName(context, it)
            onLocationFetched(locationName)
        }
    } catch (e: Exception) {
        onLocationFetched("[Lokasi tidak tersedia]")
    }
}

// ActionButton Component
@Composable
fun ActionButton(
    label: String,
    isPrimary: Boolean,
    primaryColor: Color,
    onClickAction: () -> Unit
) {
    Button(
        onClick = onClickAction,
        modifier = Modifier.size(width = 100.dp, height = 40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) primaryColor else Color.Gray,
            disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

suspend fun submitReason(
    context: Context,
    type: String,
    description: String,
    attachmentUri: Uri?
): Boolean {
    if (!hasLocationPermissions(context)) {
        println("Location permissions are not granted")
        return false
    }

    val userId = getUserId(context)
    val authToken = getAuthToken(context)
    if (userId == -1 || authToken == null) {
        println("User ID or Auth Token not found")
        return false
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    return try {
        val location = fusedLocationClient.lastLocation.await()

        location?.let { currentLocation ->
            val locationName = getLocationName(context, currentLocation)

            val checkInTime = if (type == "hadir") {
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            } else null

            // Create RequestBody objects for each field
            val userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId.toString())
            val locationIdBody = RequestBody.create(MediaType.parse("text/plain"), "1")
            val locationNameBody = locationName?.let {
                RequestBody.create(MediaType.parse("text/plain"), it)
            }
            val typeBody = RequestBody.create(MediaType.parse("text/plain"), type)
            val latitudeBody = RequestBody.create(MediaType.parse("text/plain"), currentLocation.latitude.toString())
            val longitudeBody = RequestBody.create(MediaType.parse("text/plain"), currentLocation.longitude.toString())
            val checkInTimeBody = checkInTime?.let {
                RequestBody.create(MediaType.parse("text/plain"), it)
            }
            val reasonBody = description.let {
                RequestBody.create(MediaType.parse("text/plain"), it)
            }
            val deviceNameBody = RequestBody.create(MediaType.parse("text/plain"), getDeviceName())

            // Handle attachment if present
            val attachmentPart = attachmentUri?.let { uri ->
                val stream = context.contentResolver.openInputStream(uri)
                val requestFile = stream?.let {
                    RequestBody.create(
                        MediaType.parse(context.contentResolver.getType(uri) ?: "application/octet-stream"),
                        it.readBytes()
                    )
                }
                MultipartBody.Part.createFormData(
                    "attachment",
                    "attachment_${System.currentTimeMillis()}",
                    requestFile ?: RequestBody.create(null, ByteArray(0))
                )
            }

            val response = if (attachmentUri != null) {
                ApiConfig.apiService.createAbsensiWithAttachment(
                    "Bearer $authToken",
                    userIdBody,
                    locationIdBody,
                    locationNameBody,
                    typeBody,
                    latitudeBody,
                    longitudeBody,
                    checkInTimeBody,
                    null, // checkOutTime
                    reasonBody,
                    deviceNameBody,
                    attachmentPart
                )
            } else {
                // Use normal JSON request if no attachment
                val absensiRequest = AbsensiRequest(
                    user_id = userId,
                    location_id = 1,
                    location_name = locationName,
                    type = type,
                    latitude = currentLocation.latitude,
                    longitude = currentLocation.longitude,
                    check_in_time = checkInTime,
                    check_out_time = null,
                    reason = description,
                    device_name = getDeviceName(),
                    attachment = null
                )
                ApiConfig.apiService.createAbsensi("Bearer $authToken", absensiRequest)
            }

            if (response.isSuccessful) {
                println("Attendance submitted successfully")
                return true
            } else {
                println("Failed to create attendance: ${response.errorBody()?.string()}")
                return false
            }
        } ?: false
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

// Helper function to check location permissions
private fun hasLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

// Helper extension function to convert AbsensiRequest to MultipartBody parts
private fun AbsensiRequest.toMultipartMap(): Map<String, RequestBody> {
    return mapOf(
        "user_id" to RequestBody.create(MediaType.parse("text/plain"), user_id.toString()),
        "location_id" to RequestBody.create(MediaType.parse("text/plain"), location_id.toString()),
        "type" to RequestBody.create(MediaType.parse("text/plain"), type),
        "latitude" to RequestBody.create(MediaType.parse("text/plain"), latitude.toString()),
        "longitude" to RequestBody.create(MediaType.parse("text/plain"), longitude.toString()),
        "device_name" to RequestBody.create(MediaType.parse("text/plain"), device_name)
    ).plus(
        listOfNotNull(
            location_name?.let { "location_name" to RequestBody.create(MediaType.parse("text/plain"), it) },
            reason?.let { "reason" to RequestBody.create(MediaType.parse("text/plain"), it) },
            check_in_time?.let { "check_in_time" to RequestBody.create(MediaType.parse("text/plain"), it) },
            check_out_time?.let { "check_out_time" to RequestBody.create(MediaType.parse("text/plain"), it) }
        ).toMap()
    )
}

@Preview(showBackground = true)
@Composable
fun LainnyaPreview() {
    val context = LocalContext.current
    Lainnya(navController = rememberNavController(), context = context)
}