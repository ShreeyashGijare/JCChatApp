package com.example.jetpackcomposechatapp.uiComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposechatapp.ui.theme.interFontFamily
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyMedium
import com.example.jetpackcomposechatapp.ui.theme.veryLightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldComponent(
    labelValue: String,
    leadingIcon: ImageVector,
    text: String = "",
    onTextSelected: (String) -> Unit,
    errorMessage: String = "",
    isError: Boolean = false,
    fontFamily: FontFamily = interFontFamilyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.None
    )
) {

    var textValue: String by remember {
        mutableStateOf(text)
    }

    Box(modifier = Modifier.fillMaxWidth()) {

        if (textValue.isEmpty()) {
            Text(
                text = labelValue,
                style = TextStyle(
                    fontFamily = interFontFamily
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart)
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    brush = if (isError) Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.error,
                            MaterialTheme.colorScheme.error
                        )
                    ) else Brush.linearGradient(
                        colors = listOf(
                            veryLightBlue,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    shape = RoundedCornerShape(5.dp)
                ),
            /*label = {
                Text(text = labelValue, fontFamily = fontFamily)
            },*/
            value = textValue,
            onValueChange = {
                textValue = it
                onTextSelected(it)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorTextColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            /*leadingIcon = {
                Icon(imageVector = leadingIcon, contentDescription = null)
            },*/
            isError = isError,
            shape = RoundedCornerShape(5.dp),
            textStyle = TextStyle(
                fontFamily = fontFamily
            )
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    if (isError) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .offset(y = (-8).dp)
                .fillMaxWidth(0.9f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    onTextSelected: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "",
    fontFamily: FontFamily = interFontFamilyMedium,
    onDone: () -> Unit
) {

    var textValue: String by remember {
        mutableStateOf("")
    }

    var passwordVisibility: Boolean by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (textValue.isEmpty()) {
            Text(
                text = labelValue,
                style = TextStyle(
                    fontFamily = interFontFamily
                ),
                modifier = Modifier
                    .padding(start = 51.dp)
                    .align(Alignment.CenterStart)
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    brush = if (isError) Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.error,
                            MaterialTheme.colorScheme.error
                        )
                    ) else Brush.linearGradient(
                        colors = listOf(
                            veryLightBlue,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    shape = RoundedCornerShape(5.dp)
                ),
            value = textValue,
            onValueChange = {
                textValue = it
                onTextSelected(it)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorTextColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone.invoke()
                }
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isError,
            shape = RoundedCornerShape(5.dp),
            textStyle = TextStyle(
                fontFamily = fontFamily
            )
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    if (isError) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(start = 8.dp)
                .offset(y = (-8).dp)
                .fillMaxWidth(0.9f)
        )
    }
}