package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.navigation.BottomNavigationBar
import com.example.jetpackcomposechatapp.navigation.graphs.HomeNavGraph
import com.example.jetpackcomposechatapp.uiComponents.ExtendedFloatingButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.FloatingActionButtonComponent
import com.example.jetpackcomposechatapp.utils.HomeRouteScreen
import com.example.jetpackcomposechatapp.utils.bottomNavigationItemList
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    homeNavController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavigationItemList,
                currentRoute = currentRoute
            ) { currentNavigationItem ->
                homeNavController.navigate(currentNavigationItem.route) {
                    homeNavController.graph.startDestinationRoute?.let { startDestinationRoute ->
                        popUpTo(startDestinationRoute) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        floatingActionButton = {
            if (!currentRoute.isNullOrEmpty()) {
                if (currentRoute == HomeRouteScreen.ChatListScreen.route) {
                    ExtendedFloatingButtonComponent(
                        icon = Icons.Filled.PersonAdd,
                        buttonText = R.string.add_chat
                    ) {
                        homeNavController.navigate(HomeRouteScreen.ContactsScreen.route)
                    }
                } else if (currentRoute == HomeRouteScreen.StatusListScreen.route) {
                    UpdatesScreenFloatingButtons()
                }
            }
        }
    ) { innerPadding ->
        HomeNavGraph(
            rootNavController = rootNavController,
            homeNavController = homeNavController,
            paddingValues = innerPadding
        )
    }
}


@Composable
fun UpdatesScreenFloatingButtons() {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        AnimatedTextUpdateButton {

        }
        Spacer(modifier = Modifier.height(10.dp))
        ExtendedFloatingButtonComponent(
            icon = Icons.Filled.Update,
            buttonText = R.string.add_update
        ) {

        }
    }
}

@Composable
fun AnimatedTextUpdateButton(
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100) // Optional delay before the animation starts
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it }, // Slide in from the bottom
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }, // Slide out to the bottom
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        FloatingActionButtonComponent(icon = Icons.Filled.Edit) {
            onClick()
        }
    }
}



