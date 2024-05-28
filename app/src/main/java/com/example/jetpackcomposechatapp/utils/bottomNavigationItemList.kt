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
        MainRouteScreen.ChatListScreen.route,
        selectedIcon = Icons.Filled.MarkChatRead,
        unSelectedIcon = Icons.Outlined.MarkChatRead,
        false
    ),
    BottomNavigationItem(
        "Updates",
        MainRouteScreen.StatusListScreen.route,
        selectedIcon = Icons.Filled.Update,
        unSelectedIcon = Icons.Outlined.Update,
        false
    ),
    BottomNavigationItem(
        "Profile",
        MainRouteScreen.ProfileScreen.route,
        selectedIcon = Icons.Filled.MarkChatRead,
        unSelectedIcon = Icons.Outlined.MarkChatRead,
        false
    )
)