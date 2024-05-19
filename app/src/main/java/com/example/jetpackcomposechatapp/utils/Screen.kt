package com.example.jetpackcomposechatapp.utils

sealed class Screen(val route: String) {



    object ChatScreen : Screen("Chat/{chatId}") {
        fun createRoute(id: String) = "Chat/$id"
    }


    object StatusScreen : Screen("Status/{chatId}") {
        fun createRoute(userId: String) = "Status/$userId"
    }

}