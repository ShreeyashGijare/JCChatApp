package com.example.jetpackcomposechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.navigation.graphs.authNavGraph
import com.example.jetpackcomposechatapp.screens.HomeScreen
import com.example.jetpackcomposechatapp.ui.theme.JetPackComposeChatAppTheme
import com.example.jetpackcomposechatapp.utils.Graph
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackComposeChatAppTheme {
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
    NavHost(
        navController = navController,
        startDestination = firebaseAuthCheck()
    ) {
        authNavGraph(navController)
        composable(Graph.HOME) {
            HomeScreen()
        }
    }
}

@Composable
fun firebaseAuthCheck(): String {
    return if (Firebase.auth.currentUser == null) {
        Graph.AUTHENTICATION
    } else {
        Graph.HOME
    }
}

