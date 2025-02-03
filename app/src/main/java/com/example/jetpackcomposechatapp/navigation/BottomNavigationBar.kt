package com.example.jetpackcomposechatapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposechatapp.ui.mainContent.data.bottomNavigationItem.BottomNavigationItem

/**
 * @author Coding Meet
 * Created 18-01-2024 at 01:09 pm
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    currentRoute: String?,
    onClick: (BottomNavigationItem) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                clip = true
            }
    ) {
        items.forEachIndexed { _, navigationItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                ),
                selected = currentRoute == navigationItem.route,
                onClick = { onClick(navigationItem) },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navigationItem.route) navigationItem.selectedIcon else navigationItem.unSelectedIcon,
                        contentDescription = "Navigation Icon"
                    )
                }, label = {
                    Text(text = navigationItem.title)
                },
                alwaysShowLabel = false
            )
        }
    }
}