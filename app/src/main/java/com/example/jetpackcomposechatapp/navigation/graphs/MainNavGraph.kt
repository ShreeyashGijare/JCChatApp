package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposechatapp.screens.ChatListScreen
import com.example.jetpackcomposechatapp.screens.ProfileScreen
import com.example.jetpackcomposechatapp.screens.StatusListScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.example.jetpackcomposechatapp.utils.MainRouteScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = homeNavController,
        route = Graph.MAIN_SCREEN_GRAPH,
        startDestination = MainRouteScreen.ChatListScreen.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(MainRouteScreen.ChatListScreen.route) {
            ChatListScreen(navController = rootNavController)
        }
        composable(MainRouteScreen.StatusListScreen.route) {
            StatusListScreen(navController = rootNavController)
        }
        composable(MainRouteScreen.ProfileScreen.route) {
            ProfileScreen(navController = rootNavController)
        }
    }
}