package com.example.jetpackcomposechatapp.navigation

import androidx.navigation.NavController

fun navigateUpTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}