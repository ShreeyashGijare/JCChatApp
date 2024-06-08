package com.example.jetpackcomposechatapp.uiComponents

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HeadLineMediumComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier,
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadLineSmallComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier,
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun BodyMediumComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier,
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun BodySmallComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
            .clickable { onClick() },
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LabelLargeComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier,
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LabelMediumComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier,
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LabelSmallComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier,
        color = color,
        textAlign = TextAlign.Center
    )
}