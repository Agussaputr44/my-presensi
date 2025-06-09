package com.example.mypresensi_mobile.core.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mypresensi_mobile.core.constant.ApiConfig
import com.example.mypresensi_mobile.core.model.AttendanceData
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, context: Context) {
    var historyData by remember { mutableStateOf<List<AttendanceData>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", null)
            if (token != null) {
                try {
                    val response = ApiConfig.apiService.getAbsensiByUserId("Bearer $token")
                    if (response.isSuccessful) {
                        historyData = response.body()?.data
                        if (historyData.isNullOrEmpty()) {
                            errorMessage = "Data absensi belum tersedia."
                        }
                    } else {
                        errorMessage = "Data absensi belum tersedia."
                    }
                } catch (e: Exception) {
                    errorMessage = "Terjadi kesalahan: ${e.message}"
                }
            } else {
                errorMessage = "User tidak terautentikasi"
            }
            isLoading = false
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFAFAFA)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Histori Presensi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D2D2D)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2D2D2D)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White
                ),
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    spotColor = Color.Black.copy(alpha = 0.1f)
                )
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage ?: "Unknown error", color = Color.Red)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    historyData?.let { data ->
                        items(data) { history ->
                            HistoryItem(historyData = history)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun HistoryItem(historyData: AttendanceData) {
    // Parse the createdAt date to LocalDateTime
    val createdAt = LocalDateTime.parse(historyData.createdAt, DateTimeFormatter.ISO_DATE_TIME)
    // Format the date to display the day of the week and date
    val formattedDate = createdAt.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("id")))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFFFCBA03).copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFFCBA03).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = Color(0xFFFCBA03),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D2D2D)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Status: ${historyData.type}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusColor = when {
                        historyData.status.contains("Terlambat", ignoreCase = true) -> Color(0xFFFF5252)
                        historyData.status.contains("Sakit", ignoreCase = true) -> Color(0xFFFF9800)
                        historyData.status.contains("Izin", ignoreCase = true) -> Color(0xFF2196F3)
                        else -> Color(0xFF4CAF50)
                    }

                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                        tint = statusColor
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = historyData.status,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = historyData.checkInTime ?: "N/A",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoriPreview() {
    val context = LocalContext.current
    HistoryScreen(navController = rememberNavController(), context = context)
}
