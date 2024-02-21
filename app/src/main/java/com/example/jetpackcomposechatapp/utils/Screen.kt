package com.example.jetpackcomposechatapp.utils

sealed class Screen(val route: String) {

    object SignUpScreen : Screen("SignUp")
    object LoginScreen : Screen("Login")
    object ProfileScreen : Screen("Profile")
    object ChatListScreen : Screen("ChatList")
    object ChatScreen : Screen("Chat/{chatId}") {
        fun createRoute(id: String) = "Chat/$id"
    }

    object StatusListScreen : Screen("Status")
    object StatusScreen : Screen("Status/{chatId}") {
        fun createRoute(userId: String) = "Status/$userId"
    }

}