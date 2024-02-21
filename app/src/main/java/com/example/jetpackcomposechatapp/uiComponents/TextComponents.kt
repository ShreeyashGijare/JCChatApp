package com.example.jetpackcomposechatapp.uiComponents

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HeadLineMediumComponent(
    @StringRes textValue: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = textValue),
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(8.dp),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )

}