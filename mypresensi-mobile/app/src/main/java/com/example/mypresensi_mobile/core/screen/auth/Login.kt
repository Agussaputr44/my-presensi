package com.example.mypresensi_mobile.core.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mypresensi_mobile.core.model.LoginRequest
import com.example.mypresensi_mobile.core.service.ApiService
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.mypresensi_mobile.R
import com.example.mypresensi_mobile.core.constant.ApiConfig
import com.example.mypresensi_mobile.core.screen.Lainnya

@Composable
fun LoginScreen(navController: NavController, apiService: ApiService) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

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
                color = Color(0xFF2D2D2D)
            )

            Text(
                text = "Silakan masuk ke akun Anda",
                fontSize = 16.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFFFCBA03)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFFCBA03)
                    )
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                            tint = Color(0xFF666666)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            // Membuat objek LoginRequest dengan email dan password
                            val loginRequest = LoginRequest(email = email, password = password)

                            // Mengirim request login menggunakan objek LoginRequest
                            val response = apiService.login(loginRequest)

                            if (response.isSuccessful) {
                                val token = response.body()?.token // Ambil token dari respons API
                                val userId = response.body()?.user?.id

                                Log.d("LoginResponse", "Token: $token")

                                token?.let {
                                    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("auth_token", token)
                                    editor.putInt("user_id", userId ?: -1) // Save user_id with default -1 if null
                                    editor.apply()
                                    Log.d("SharedPreferences", "Token disimpan dengan kunci 'auth_token'")
                                }

                                // Navigasi jika login berhasil
                                navController.navigate("beranda")
                            } else {
                                Log.e("LoginError", "Error: ${response.code()} - ${response.message()}")
                            }
                        } catch (e: Exception) {
                            Log.e("LoginException", "Exception: ${e.message}")
                        } finally {
                            isLoading = false
                        }
                    }


        },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCBA03)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "MASUK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Link
            TextButton(
                onClick = { navController.navigate("register") }
            ) {
                Text(
                    text = "Belum punya akun? Daftar",
                    color = Color(0xFFFCBA03),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val apiService = ApiConfig.apiService
    LoginScreen(navController = rememberNavController(),  apiService = apiService)
}
