package com.example.jetpackcomposechatapp.ui.loginSignUp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.SignUpEvents
import com.example.jetpackcomposechatapp.ui.loginSignUp.viewmodel.SignUpViewModel
import com.example.jetpackcomposechatapp.ui.theme.colorBlue
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.uiComponents.CommonProgressBar
import com.example.jetpackcomposechatapp.uiComponents.PinkBackgroundButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.HeadLineMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.OutlinedTextFieldComponent
import com.example.jetpackcomposechatapp.uiComponents.PasswordTextFieldComponent
import com.example.jetpackcomposechatapp.utils.AuthRouteScreen
import com.example.jetpackcomposechatapp.utils.Graph

@Composable
fun SignUpScreen(
    rootNavController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .background(colorBlue)
            .padding(horizontal = 20.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.chat_icon_one),
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        HeadLineMediumComponent(
            textValue = stringResource(id = R.string.sign_up),
            modifier = Modifier.heightIn(80.dp)
        )
        Spacer(modifier = Modifier.heightIn(20.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Name",
            leadingIcon = Icons.Default.AccountCircle,
            onTextSelected = {
                viewModel.onEvent(SignUpEvents.Name(it))
            },
            isError = signUpState.nameError,
            errorMessage = signUpState.nameErrorMessage
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Number",
            leadingIcon = Icons.Default.PhoneAndroid,
            onTextSelected = {
                viewModel.onEvent(SignUpEvents.Number(it))
            },
            isError = signUpState.numberError,
            errorMessage = signUpState.numberErrorMessage,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Email",
            leadingIcon = Icons.Default.Email,
            onTextSelected = {
                viewModel.onEvent(SignUpEvents.Email(it))
            },
            isError = signUpState.emailError,
            errorMessage = signUpState.emailErrorMessage,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        PasswordTextFieldComponent(
            labelValue = "Enter your Password", onTextSelected = {
                viewModel.onEvent(SignUpEvents.Password(it))
            },
            isError = signUpState.passwordError,
            errorMessage = signUpState.passwordErrorMessage
        )
        Spacer(modifier = Modifier.heightIn(30.dp))
        PinkBackgroundButtonComponent(
            buttonText = R.string.sign_up,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            viewModel.onEvent(SignUpEvents.SignUpButtonClick)
        }
        Spacer(modifier = Modifier.heightIn(10.dp))
        BodySmallComponent(textValue = stringResource(id = R.string.already_have_account)) {
            rootNavController.popBackStack()
            navigateUpTo(rootNavController, AuthRouteScreen.LoginScreen.route)
        }

    }
    if (viewModel.inProgress.value) {
        CommonProgressBar()
    }
}
