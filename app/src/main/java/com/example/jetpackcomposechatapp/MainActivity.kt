package com.example.jetpackcomposechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.screens.ChatListScreen
import com.example.jetpackcomposechatapp.screens.LoginScreen
import com.example.jetpackcomposechatapp.screens.SignUpScreen
import com.example.jetpackcomposechatapp.ui.theme.JetPackComposeChatAppTheme
import com.example.jetpackcomposechatapp.utils.Screen
import com.example.jetpackcomposechatapp.viewModel.SignUpViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    val context = LocalContext.current
    val backDispatcher = remember(LocalLifecycleOwner.current) { OnBackPressedDispatcher() }

    BackHandler {
        when (navController.currentBackStackEntry?.destination?.route) {

            Screen.SignUpScreen.route -> {
                (context as? ComponentActivity)?.finishAffinity()
            }

            Screen.LoginScreen.route -> {
                navController.popBackStack()
            }

        }
    }

    NavHost(
        navController = navController,
        startDestination = firebaseAuthCheck()
    ) {
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController)
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.ChatListScreen.route) {
            ChatListScreen()
        }
    }
}

@Composable
fun firebaseAuthCheck(

): String {
    val userLiveData: LiveData<FirebaseUser?> = MutableLiveData<FirebaseUser?>().apply {
        value = Firebase.auth.currentUser
    }
    val currentUserState by remember { mutableStateOf(userLiveData) }

    return if (Firebase.auth.currentUser == null) {
        Screen.SignUpScreen.route
    } else {
        Screen.ChatListScreen.route
    }
}


