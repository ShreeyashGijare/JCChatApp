package com.example.jetpackcomposechatapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposechatapp.ui.mainContent.screens.MainScreen
import com.example.jetpackcomposechatapp.utils.Graph
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RootNavGraph() {

    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        route = Graph.ROOT_GRAPH,
        startDestination = firebaseAuthCheck()
    ) {
        composable(route = Graph.MAIN_SCREEN_GRAPH) {
            MainScreen(rootNavController)
        }
        authNavGraph(rootNavController)
    }
}

@Composable
fun firebaseAuthCheck(): String {
    return if (Firebase.auth.currentUser == null) {
        Graph.AUTHENTICATION_GRAPH
    } else {
        Graph.MAIN_SCREEN_GRAPH
    }
}