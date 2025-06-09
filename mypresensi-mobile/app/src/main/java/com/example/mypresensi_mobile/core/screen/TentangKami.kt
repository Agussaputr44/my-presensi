package com.example.mypresensi_mobile.core.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TentangKami(navController: NavController) {
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TopAppBar with shadow
            TopAppBar(
                title = {
                    Text(
                        text = "Tentang Kami",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D2D2D)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2D2D2D)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    spotColor = Color.Black.copy(alpha = 0.1f)
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo Container
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = Color(0xFFFCBA03).copy(alpha = 0.3f)
                        )
                        .clip(RoundedCornerShape(24.dp))
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
                    Text(
                        text = "MP",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Title with enhanced styling
                Text(
                    text = "MY PRESENSI",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "Solusi Presensi Digital Modern",
                    fontSize = 18.sp,
                    color = Color(0xFF666666),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Description Card
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "My Presensi adalah aplikasi modern untuk manajemen kehadiran karyawan. Dilengkapi dengan fitur GPS dan timestamp untuk memastikan akurasi presensi digital Anda.",
                            fontSize = 16.sp,
                            color = Color(0xFF4A4A4A),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Contact Information
                ContactInfoItem(
                    icon = Icons.Filled.LocationOn,
                    text = "Jl. Jendral Sudirman, SelatBaru"
                )

                Spacer(modifier = Modifier.height(12.dp))

                ContactInfoItem(
                    icon = Icons.Filled.House,
                    text = "SDN 1 Bantan"
                )

                Spacer(modifier = Modifier.height(12.dp))

                ContactInfoItem(
                    icon = Icons.Filled.MailOutline,
                    text = "info@mypresensi.com"
                )
            }
        }
    }
}

@Composable
fun ContactInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFFFCBA03),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFF666666)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TentangKamiPreview() {
    TentangKami(navController = rememberNavController())
}
