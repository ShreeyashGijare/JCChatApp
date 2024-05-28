package com.example.jetpackcomposechatapp.utils

object Graph {
    const val ROOT_GRAPH = "ROOT_GRAPH"
    const val AUTHENTICATION_GRAPH = "AUTH_GRAPH"
    const val MAIN_SCREEN_GRAPH = "HOME_GRAPH"
}

sealed class AuthRouteScreen(val route: String) {
    object SignUpScreen : AuthRouteScreen("SignUp")
    object LoginScreen : AuthRouteScreen("Login")

}

sealed class MainRouteScreen(val route: String) {

    object ProfileScreen : MainRouteScreen("Profile")
    object ChatListScreen : MainRouteScreen("ChatList")
    object StatusListScreen : MainRouteScreen("Status")
    object ContactsScreen: MainRouteScreen("Contacts")

}