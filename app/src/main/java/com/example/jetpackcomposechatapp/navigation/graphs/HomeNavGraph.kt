package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ChatListScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ChatScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ContactsScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ProfileScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.StatusListScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.example.jetpackcomposechatapp.utils.HomeRouteScreen
import com.google.gson.Gson

@Composable
fun HomeNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = homeNavController,
        route = Graph.HOME_GRAPH,
        startDestination = HomeRouteScreen.ChatListScreen.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(
            HomeRouteScreen.ChatListScreen.route,
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            },
            exitTransition = {
                fadeOut(
                    targetAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        ) {
            ChatListScreen(navController = rootNavController)
        }

        composable(
            HomeRouteScreen.StatusListScreen.route,
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            },
            exitTransition = {
                fadeOut(
                    targetAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        ) {
            StatusListScreen(navController = rootNavController)
        }

        composable(
            HomeRouteScreen.ProfileScreen.route,
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            },
            exitTransition = {
                fadeOut(
                    targetAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        ) {
            ProfileScreen(navController = rootNavController)
        }

        composable(
            HomeRouteScreen.ContactsScreen.route,
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            },
            exitTransition = {
                fadeOut(
                    targetAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        ) {
            ContactsScreen(homeNavController = homeNavController)
        }

        composable(
            "${HomeRouteScreen.ChatScreen.route}?userData={userData}",
            arguments = listOf(
                navArgument(
                    name = "userData"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }),
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            },
            exitTransition = {
                fadeOut(
                    targetAlpha = 0.3f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        ) {
            val userData =
                Gson().fromJson(it.arguments?.getString("userData"), UserData::class.java)
            ChatScreen(homeNavController = homeNavController, userData = userData)
        }
    }
}