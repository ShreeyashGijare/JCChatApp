package com.example.jetpackcomposechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.data.bottomNavigationItem.BottomNavigationItem
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.screens.ChatListScreen
import com.example.jetpackcomposechatapp.screens.LoginScreen
import com.example.jetpackcomposechatapp.screens.ProfileScreen
import com.example.jetpackcomposechatapp.screens.SignUpScreen
import com.example.jetpackcomposechatapp.screens.StatusListScreen
import com.example.jetpackcomposechatapp.ui.theme.JetPackComposeChatAppTheme
import com.example.jetpackcomposechatapp.utils.Screen
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

    companion object {
        const val AUTH_NAV_GRAPH = "Auth"
        const val MAIN_NAV_GRAPH = "Main"
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    BackHandler {
        when (navController.currentBackStackEntry?.destination?.route) {
            Screen.SignUpScreen.route -> {
                (context as MainActivity).finish()
            }

            Screen.LoginScreen.route -> {
                (context as MainActivity).finish()
            }

            Screen.ChatListScreen.route -> {
                (context as MainActivity).finish()
            }

            Screen.StatusListScreen.route -> {
                selectedItemIndex = 0
                navigateUpTo(navController, Screen.ChatListScreen.route)
            }

            Screen.ProfileScreen.route -> {
                selectedItemIndex = 0
                navigateUpTo(navController, Screen.ChatListScreen.route)
            }
        }
    }

    showBottomBar = when (navBackStackEntry.value?.destination?.route) {
        Screen.ChatListScreen.route -> true
        Screen.ProfileScreen.route -> true
        Screen.StatusListScreen.route -> true
        else -> {
            false
        }
    }

    BottomNavigation(
        navController = navController,
        selectedItemIndex = selectedItemIndex,
        onItemSelected = {
            selectedItemIndex = it
        },
        content = {
        },
        showBottomBar = showBottomBar
    )

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
            ChatListScreen(navController)
        }
        composable(Screen.StatusListScreen.route) {
            StatusListScreen(navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(/*navController*/)
        }
    }


}

@Composable
fun firebaseAuthCheck(): String {
    return if (Firebase.auth.currentUser == null) {
        Screen.SignUpScreen.route
    } else {
        Screen.ChatListScreen.route
    }
}


@Composable
fun BottomNavigation(
    navController: NavController = rememberNavController(),
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    content: @Composable (Modifier) -> Unit,
    showBottomBar: Boolean
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) NavigationBar {
                BottomNavigationItem.getItemList().forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            onItemSelected(index)
//                            navController.navigate(item.route)
                            navigateUpTo(navController, item.route)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unSelectedIcon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = item.title)
                        })
                }
            }
        }
    ) {
        content(Modifier.padding(it))
    }
}


