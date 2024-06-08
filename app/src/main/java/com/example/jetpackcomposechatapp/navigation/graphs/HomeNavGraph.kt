package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ChatListScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ContactsScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.ProfileScreen
import com.example.jetpackcomposechatapp.ui.mainContent.screens.StatusListScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.example.jetpackcomposechatapp.utils.HomeRouteScreen

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
            ContactsScreen(navController = homeNavController)
        }
    }
}