package com.example.jetpackcomposechatapp.utils

object Graph {
    const val ROOT_GRAPH = "ROOT_GRAPH"
    const val AUTHENTICATION_GRAPH = "AUTH_GRAPH"
    const val HOME_GRAPH = "HOME_GRAPH"
}

sealed class AuthRouteScreen(val route: String) {

    object OnBoardScreen : AuthRouteScreen("OnBoard")
    object SignUpScreen : AuthRouteScreen("SignUp")
    object LoginScreen : AuthRouteScreen("Login")

}

sealed class HomeRouteScreen(val route: String) {

    object ProfileScreen : HomeRouteScreen("Profile")
    object ChatListScreen : HomeRouteScreen("ChatList")
    object StatusListScreen : HomeRouteScreen("Status")

    object ContactsScreen : HomeRouteScreen("Contacts")
    object ChatScreen : HomeRouteScreen("Chat/{userData}")


}