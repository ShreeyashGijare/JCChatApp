package com.example.jetpackcomposechatapp.data.bottomNavigationItem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.MarkChatRead
import androidx.compose.material.icons.outlined.Update
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetpackcomposechatapp.utils.Screen

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNew: Boolean,
    val badgeCount: Int? = null
) {

    companion object {
        fun getItemList() = listOf(
            BottomNavigationItem(
                "Chat",
                Screen.ChatListScreen.route,
                selectedIcon = Icons.Filled.MarkChatRead,
                unSelectedIcon = Icons.Outlined.MarkChatRead,
                false
            ),
            BottomNavigationItem(
                "Updates",
                Screen.StatusListScreen.route,
                selectedIcon = Icons.Filled.Update,
                unSelectedIcon = Icons.Outlined.Update,
                false
            ),
            BottomNavigationItem(
                "Profile",
                Screen.ProfileScreen.route,
                selectedIcon = Icons.Filled.MarkChatRead,
                unSelectedIcon = Icons.Outlined.MarkChatRead,
                false
            )
        )
    }
}
