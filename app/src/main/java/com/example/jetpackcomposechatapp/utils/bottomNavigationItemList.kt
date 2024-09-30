package com.example.jetpackcomposechatapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.MarkChatRead
import androidx.compose.material.icons.outlined.Update
import com.example.jetpackcomposechatapp.ui.mainContent.data.bottomNavigationItem.BottomNavigationItem

val bottomNavigationItemList = listOf(
    BottomNavigationItem(
        "Chat",
        HomeRouteScreen.ChatListScreen.route,
        selectedIcon = Icons.Filled.MarkChatRead,
        unSelectedIcon = Icons.Outlined.MarkChatRead,
        false
    ),
    BottomNavigationItem(
        "Updates",
        HomeRouteScreen.StatusListScreen.route,
        selectedIcon = Icons.Filled.Update,
        unSelectedIcon = Icons.Outlined.Update,
        false
    ),
    BottomNavigationItem(
        "Profile",
        HomeRouteScreen.ProfileScreen.route,
        selectedIcon = Icons.Filled.MarkChatRead,
        unSelectedIcon = Icons.Outlined.MarkChatRead,
        false
    )
)