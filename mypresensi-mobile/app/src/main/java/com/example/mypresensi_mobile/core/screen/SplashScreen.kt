package com.example.mypresensi_mobile.core.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay


@Composable
@Preview
fun SplashScreen() {
    val context = LocalContext.current

    // Background color for the splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Change to your preferred background color
    ) {
        // Centering the text
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            // Animating text scale
            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
//
//            // Logo Text with custom styling and animation
//            Text(
//                text = "MyPresensi",
//                style = TextStyle(
//                    fontStyle = FontStyle.Italic,
//                    fontSize = 55.sp,
//                    fontWeight = FontWeight.ExtraBold,
//                    color = Color(0xFFFCBA03),
//                    shadow = Shadow(
//                        color = Color.White,
//                        blurRadius = 6f
//                    )
//
//                ),
//                modifier = Modifier.scale(scale)
//            )
        }
    }
//
//    // Delay and navigate to the next screen after animation
//    LaunchedEffect(Unit) {
//        delay(2000) // Wait for the animation to finish
//        // Navigate to the next screen (replace `Beranda::class.java` with your next activity)
//        val intent = Intent(/* packageContext = */ context, /* cls = */ Beranda().javaClass) // Replace with your next activity class
//        context.startActivity(intent)
//        // Optionally, you can also call finish() here to close the SplashActivity
//        (context as? ComponentActivity)?.finish()
//    }
}
