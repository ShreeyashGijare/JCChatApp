package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposechatapp.screens.ChatListScreen
import com.example.jetpackcomposechatapp.screens.ProfileScreen
import com.example.jetpackcomposechatapp.screens.StatusListScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.example.jetpackcomposechatapp.utils.Screen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = Screen.ChatListScreen.route
    ) {
        composable(Screen.ChatListScreen.route) {
            ChatListScreen(navController = navController)
        }
        composable(Screen.StatusListScreen.route) {
            StatusListScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
    }
}