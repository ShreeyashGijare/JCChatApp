package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jetpackcomposechatapp.ui.loginSignUp.screens.LoginScreen
import com.example.jetpackcomposechatapp.ui.loginSignUp.screens.SignUpScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.example.jetpackcomposechatapp.utils.Screen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screen.SignUpScreen.route
    ) {
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
    }
}