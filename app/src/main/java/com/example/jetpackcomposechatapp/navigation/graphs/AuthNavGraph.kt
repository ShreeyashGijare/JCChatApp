package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jetpackcomposechatapp.ui.loginSignUp.screens.LoginScreen
import com.example.jetpackcomposechatapp.ui.loginSignUp.screens.SignUpScreen
import com.example.jetpackcomposechatapp.utils.AuthRouteScreen
import com.example.jetpackcomposechatapp.utils.Graph

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION_GRAPH,
        startDestination = AuthRouteScreen.SignUpScreen.route
    ) {
        composable(AuthRouteScreen.SignUpScreen.route) {
            SignUpScreen(rootNavController = rootNavController)
        }
        composable(AuthRouteScreen.LoginScreen.route) {
            LoginScreen(rootNavController = rootNavController)
        }
    }
}