package com.example.jetpackcomposechatapp.uiComponents

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun ExtendedFloatingButtonComponent(
    icon: ImageVector,
    buttonText: Int,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        onClick = {
            onClick()
        },
        icon = { Icon(icon, stringResource(id = buttonText)) },
        text = { Text(text = stringResource(id = buttonText)) },
    )
}


@Composable
fun FloatingActionButtonComponent(
    icon: ImageVector,
    onClick: () -> Unit
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.clip(CircleShape),
        onClick = { onClick() }
    ) {
        Icon(icon, "Small floating action button.")
    }
}