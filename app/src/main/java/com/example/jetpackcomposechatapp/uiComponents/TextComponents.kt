package com.example.jetpackcomposechatapp.uiComponents

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.jetpackcomposechatapp.ui.theme.interFontFamily

@Composable
fun HeadLineLargeComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun HeadLineMediumComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun HeadLineSmallComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun BodyLargeComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun BodyMediumComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun BodySmallComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily,
    onClick: () -> Unit
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
            .clickable { onClick() },
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun LabelLargeComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun LabelMediumComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun LabelSmallComponent(
    textValue: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = interFontFamily
) {
    Text(
        text = textValue,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}