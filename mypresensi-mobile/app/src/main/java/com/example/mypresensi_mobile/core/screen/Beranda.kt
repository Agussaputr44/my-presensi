package com.example.mypresensi_mobile.core.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mypresensi_mobile.R
import com.example.mypresensi_mobile.core.constant.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Beranda(navController: NavController, userLocation: String, context: Context) {
    var userName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "")
        if (token.isNullOrEmpty()) {
            errorMessage = "Token is missing"
            return@LaunchedEffect
        }

        try {
            val response = ApiConfig.apiService.getUser("Bearer $token")
            if (response.isSuccessful) {
                userName = response.body()?.name ?: "Unknown User"
            } else {
                errorMessage = "Failed to fetch user data"
            }
        } catch (e: Exception) {
            errorMessage = "Exception: ${e.message}"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFBF2),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderSection(userName, errorMessage)
            Spacer(modifier = Modifier.height(48.dp))
            ButtonSection(navController = navController, context = context)
        }
    }
}

@Composable
fun HeaderSection(userName: String, errorMessage: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFCBA03),
                            Color(0xFFFCD303)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Ganti 'logo' dengan nama file di drawable
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp) // Sesuaikan ukuran gambar
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Selamat Datang",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D2D2D),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D2D2D).copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonSection(navController: NavController, context: Context) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ButtonItem(
            label = "PRESENSI",
            onClickAction = { navController.navigate("absensi") }
        )
        ButtonItem(
            label = "HISTORY",
            onClickAction = { navController.navigate("history") }
        )
        ButtonItem(
            label = "TENTANG KAMI",
            onClickAction = { navController.navigate("tentangKami") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ButtonItem(
            label = "LOGOUT",
            isLogout = true,
            onClickAction = { showLogoutDialog = true }
        )

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Keluar") },
                text = { Text("Apakah anda yakin untuk keluar?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            performLogout(navController, context)
                        }
                    ) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Tidak")
                    }
                }
            )
        }
    }
}

@Composable
fun ButtonItem(
    label: String,
    isLogout: Boolean = false,
    onClickAction: () -> Unit
) {
    Button(
        onClick = onClickAction,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isLogout) Color.Red.copy(alpha = 0.3f) else Color(0xFFFCBA03).copy(alpha = 0.3f)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLogout) Color(0xFFFF5252) else Color(0xFFFCBA03)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isLogout) Color.White else Color(0xFF2D2D2D)
        )
    }
}

private fun performLogout(navController: NavController, context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("auth_token", null)

    if (token != null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.apiService.logout("Bearer $token")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Hapus token dari SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.remove("auth_token")
                        editor.apply()

                        Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()

                        // Navigasi ke halaman login
                        navController.navigate("login") {
                            popUpTo("beranda") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Logout failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    } else {
        Toast.makeText(context, "Token not found, please log in again.", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaPreview() {
    val context = LocalContext.current
    Beranda(navController = rememberNavController(),"", context = context)
}
