package com.example.consecutivepractices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.consecutivepractices.ui.ShopApp
import com.example.consecutivepractices.ui.theme.ConsecutivePracticesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ConsecutivePracticesTheme {

                ShopApp()
            }
        }
    }
}