package com.example.jetpackcomposechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.screens.LoginScreen
import com.example.jetpackcomposechatapp.screens.SignUpScreen
import com.example.jetpackcomposechatapp.ui.theme.JetPackComposeChatAppTheme
import com.example.jetpackcomposechatapp.utils.Constants
import com.example.jetpackcomposechatapp.utils.Screen
import com.example.jetpackcomposechatapp.viewModel.ChatViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackComposeChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: ChatViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.SignUpScreen.route) {

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController)
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen()
        }
    }
}


