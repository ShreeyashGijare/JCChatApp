package com.example.jetpackcomposechatapp.ui.loginSignUp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.SignUpEvents
import com.example.jetpackcomposechatapp.ui.loginSignUp.viewmodel.SignUpViewModel
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyExtraBold
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyMedium
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilySemiBold
import com.example.jetpackcomposechatapp.uiComponents.BlueBackgroundButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.BodyMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.DisplayLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.HeadLineMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.OutlinedTextFieldComponent
import com.example.jetpackcomposechatapp.uiComponents.PasswordTextFieldComponent
import com.example.jetpackcomposechatapp.utils.Graph

@Composable
fun SignUpScreen(
    rootNavController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var showSecondScreen by remember {
        mutableStateOf(false)
    }

    BackHandler {
        if (showSecondScreen) {
            showSecondScreen = false
        } else {
            rootNavController.navigateUp()
        }
    }

    val signUpState by viewModel.signUpState
    val signUpSuccess by viewModel.signInSuccess.collectAsState()

    LaunchedEffect(key1 = signUpSuccess) {
        if (signUpSuccess) {
            rootNavController.navigate(Graph.HOME_GRAPH) {
                popUpTo(Graph.AUTHENTICATION_GRAPH) {
                    inclusive = true
                }
            }
        } else {

        }
    }

    BasicDetailsScreen(
        onNameChanged = {
            viewModel.onEvent(SignUpEvents.Name(it))
        },
        isNameError = signUpState.nameError,
        nameErrorMessage = signUpState.nameErrorMessage,
        onNumberChanged = {
            viewModel.onEvent(SignUpEvents.Number(it))
        },
        isNumberError = signUpState.numberError,
        numberErrorMessage = signUpState.numberErrorMessage,
        onContinueClick = {
            if (viewModel.validateName() && viewModel.validateNumber()) {
                showSecondScreen = true
            } else {
                viewModel.validateName()
                viewModel.validateNumber()
            }
        }
    )

    AnimatedVisibility(
        visible = showSecondScreen,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
    ) {
        EmailPasswordScreen(
            onEmailChanged = {
                viewModel.onEvent(SignUpEvents.Email(it))
            },
            isEmailError = signUpState.emailError,
            emailErrorMessage = signUpState.emailErrorMessage,
            onPasswordChanged = {
                viewModel.onEvent(SignUpEvents.Password(it))
            },
            isPasswordError = signUpState.passwordError,
            passwordErrorMessage = signUpState.passwordErrorMessage,
            onContinueClick = {

            }
        )
    }
}

@Composable
fun BasicDetailsScreen(
    onNameChanged: (String) -> Unit,
    isNameError: Boolean,
    nameErrorMessage: String,
    onNumberChanged: (String) -> Unit,
    isNumberError: Boolean,
    numberErrorMessage: String,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 20.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        DisplayLargeComponent(
            textValue = stringResource(id = R.string.app_name),
            fontFamily = interFontFamilyExtraBold,
            color = MaterialTheme.colorScheme.tertiary
        )
        HeadLineMediumComponent(
            textValue = stringResource(R.string.app_slogan),
            fontFamily = interFontFamilySemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
        BodyMediumComponent(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            textValue = stringResource(R.string.what_should_you_be_called),
            fontFamily = interFontFamilyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Name",
            leadingIcon = Icons.Default.AccountCircle,
            onTextSelected = {
                onNameChanged(it)
            },
            isError = isNameError,
            errorMessage = nameErrorMessage
        )
        Spacer(modifier = Modifier.height(30.dp))
        BodyMediumComponent(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            textValue = stringResource(R.string.enter_mobile_text),
            fontFamily = interFontFamilyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Number",
            leadingIcon = Icons.Default.PhoneAndroid,
            onTextSelected = {
                onNumberChanged(it)
            },
            isError = isNumberError,
            errorMessage = numberErrorMessage,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.fillMaxHeight(.8f))
        BlueBackgroundButtonComponent(
            buttonText = R.string.continue_text
        ) {
            onContinueClick.invoke()
        }
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
    }
}

@Composable
fun EmailPasswordScreen(
    onEmailChanged: (String) -> Unit,
    isEmailError: Boolean,
    emailErrorMessage: String,
    onPasswordChanged: (String) -> Unit,
    isPasswordError: Boolean,
    passwordErrorMessage: String,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 20.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        DisplayLargeComponent(
            textValue = stringResource(id = R.string.app_name),
            fontFamily = interFontFamilyExtraBold,
            color = MaterialTheme.colorScheme.tertiary
        )
        HeadLineMediumComponent(
            textValue = stringResource(R.string.app_slogan),
            fontFamily = interFontFamilySemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
        BodyMediumComponent(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            textValue = stringResource(R.string.what_should_you_be_called),
            fontFamily = interFontFamilyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextFieldComponent(
            labelValue = stringResource(R.string.enter_email),
            leadingIcon = Icons.Default.AccountCircle,
            onTextSelected = {
                onEmailChanged(it)
            },
            isError = isEmailError,
            errorMessage = emailErrorMessage
        )
        Spacer(modifier = Modifier.height(30.dp))
        BodyMediumComponent(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            textValue = stringResource(R.string.enter_mobile_text),
            fontFamily = interFontFamilyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        PasswordTextFieldComponent(
            labelValue = stringResource(R.string.enter_password), onTextSelected = {
                onPasswordChanged(it)
            },
            isError = isPasswordError,
            errorMessage = passwordErrorMessage,
            onDone = {

            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(.8f))
        BlueBackgroundButtonComponent(
            buttonText = R.string.continue_text
        ) {
            onContinueClick.invoke()
        }
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
    }
}
