package com.example.rpgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rpgapp.navigation.AppNavigation
import com.example.rpgapp.ui.theme.RpgTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RpgTheme {
                AppNavigation()
            }
        }
    }
}
