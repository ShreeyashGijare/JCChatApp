package com.example.jetpackcomposechatapp.ui.mainContent.data.bottomNavigationItem

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNew: Boolean,
    val badgeCount: Int? = null
)
