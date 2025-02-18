package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.navigation.graphs.HomeNavGraph
import com.example.jetpackcomposechatapp.uiComponents.ExtendedFloatingButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.FloatingActionButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.JumpingBottomBar
import com.example.jetpackcomposechatapp.utils.HomeRouteScreen
import com.example.jetpackcomposechatapp.utils.bottomNavigationItemList
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    homeNavController: NavHostController = rememberNavController()
) {

    val context = LocalContext.current
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }

    var isFloatingButtonVisible by remember {
        mutableStateOf(false)
    }

    var bottomAppBarVisible by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            bottomNavigationItemList.find { it.route == currentRoute }?.let {
                JumpingBottomBar(
                    items = bottomNavigationItemList,
                    selected = it,
                    onJump = { currentNavigationItem ->
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
                )
            }

            if (!currentRoute.isNullOrEmpty()) {
                bottomAppBarVisible = currentRoute == HomeRouteScreen.ChatListScreen.route ||
                        currentRoute == HomeRouteScreen.StatusListScreen.route ||
                        currentRoute == HomeRouteScreen.ProfileScreen.route
            }

            isFloatingButtonVisible =
                currentRoute == HomeRouteScreen.StatusListScreen.route
        },
        floatingActionButton = {
            currentRoute?.let { currentRoute ->
                if (currentRoute == HomeRouteScreen.ChatListScreen.route || currentRoute == HomeRouteScreen.StatusListScreen.route) {
                    UpdatesScreenFloatingButtons(
                        currentRoute = currentRoute,
                        isVisible = isFloatingButtonVisible,
                        onPrimaryButtonClick = {
                            if (currentRoute == HomeRouteScreen.ChatListScreen.route) {
                                bottomAppBarVisible = !bottomAppBarVisible
                                homeNavController.navigate(HomeRouteScreen.ContactsScreen.route)
                            } else {
                                Toast.makeText(context, "Working correctly", Toast.LENGTH_LONG)
                                    .show()
                            }
                        },
                        onSecondaryButtonClick = {

                        }
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        HomeNavGraph(
            rootNavController = rootNavController,
            homeNavController = homeNavController,
            paddingValues = innerPadding,
            onShowSnackBar = { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }
}


@Composable
fun UpdatesScreenFloatingButtons(
    currentRoute: String,
    isVisible: Boolean,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        AnimatedTextUpdateButton(isVisible = isVisible) {
            onSecondaryButtonClick()
        }
        Spacer(modifier = Modifier.height(10.dp))
        ExtendedFloatingButtonComponent(
            icon = if (currentRoute == HomeRouteScreen.StatusListScreen.route) Icons.Filled.Update else Icons.Filled.PersonAdd,
            buttonText = if (currentRoute == HomeRouteScreen.StatusListScreen.route) R.string.add_update else R.string.add_chat
        ) {
            onPrimaryButtonClick()
        }
    }
}

@Composable
fun AnimatedTextUpdateButton(
    isVisible: Boolean,
    onSecondaryButtonClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        FloatingActionButtonComponent(icon = Icons.Filled.Edit) {
            onSecondaryButtonClick()
        }
    }
}