package com.example.jetpackcomposechatapp.ui.loginSignUp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.LoginEvents
import com.example.jetpackcomposechatapp.ui.loginSignUp.viewmodel.LoginViewModel
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.uiComponents.CommonProgressBar
import com.example.jetpackcomposechatapp.uiComponents.GradientButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.HeadLineMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.OutlinedTextFieldComponent
import com.example.jetpackcomposechatapp.uiComponents.PasswordTextFieldComponent
import com.example.jetpackcomposechatapp.utils.AuthRouteScreen
import com.example.jetpackcomposechatapp.utils.Graph

@Composable
fun LoginScreen(
    rootNavController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState
    val loginSuccess by viewModel.logInSuccess.collectAsState()

    LaunchedEffect(key1 = loginSuccess) {
        if (loginSuccess) {
            rootNavController.navigate(Graph.HOME_GRAPH) {
                popUpTo(Graph.AUTHENTICATION_GRAPH  ) {
                    inclusive = true
                }
            }
        } else {

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(horizontal = 20.dp),
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
        HeadLineMediumComponent(textValue = stringResource(id = R.string.sign_in), modifier = Modifier.heightIn(80.dp))
        Spacer(modifier = Modifier.heightIn(20.dp))

        OutlinedTextFieldComponent(
            labelValue = "Enter your Email",
            leadingIcon = Icons.Default.Email,
            onTextSelected = {
                viewModel.onEvent(LoginEvents.Email(it))
            },
            isError = loginState.emailError,
            errorMessage = loginState.emailErrorMessage,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        PasswordTextFieldComponent(
            labelValue = "Enter your Password", onTextSelected = {
                viewModel.onEvent(LoginEvents.Password(it))
            },
            isError = loginState.passwordError,
            errorMessage = loginState.passwordErrorMessage
        )
        Spacer(modifier = Modifier.heightIn(30.dp))
        GradientButtonComponent(
            buttonText = R.string.sign_in,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            viewModel.onEvent(LoginEvents.LoginButtonClick)
        }
        Spacer(modifier = Modifier.heightIn(10.dp))
        BodySmallComponent(textValue = stringResource(id = R.string.create_account)) {
            rootNavController.popBackStack()
            navigateUpTo(rootNavController, AuthRouteScreen.SignUpScreen.route)
        }

    }
    if (viewModel.inProgress.value) {
        CommonProgressBar()
    }

}