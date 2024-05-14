package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposechatapp.screens.HomeScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = firebaseAuthCheck(),
        route = Graph.ROOT
    ) {

        authNavGraph(navHostController)
        composable(route = Graph.HOME) {
            HomeScreen()
        }
    }
}

@Composable
fun firebaseAuthCheck(): String {
    return if (Firebase.auth.currentUser == null) {
        Graph.AUTHENTICATION
    } else {
        Graph.HOME
    }
}