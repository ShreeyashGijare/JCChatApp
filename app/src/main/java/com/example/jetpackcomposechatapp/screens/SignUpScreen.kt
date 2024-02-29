package com.example.jetpackcomposechatapp.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.signUpData.UIEvents
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.uiComponents.CommonProgressBar
import com.example.jetpackcomposechatapp.uiComponents.GradientButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.HeadLineMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.OutlinedTextFieldComponent
import com.example.jetpackcomposechatapp.uiComponents.PasswordTextFieldComponent
import com.example.jetpackcomposechatapp.utils.Screen
import com.example.jetpackcomposechatapp.viewModel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val signUpState by viewModel.signUpState

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
        HeadLineMediumComponent(textValue = R.string.sign_up, modifier = Modifier.heightIn(80.dp))
        Spacer(modifier = Modifier.heightIn(20.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Name",
            leadingIcon = Icons.Default.AccountCircle,
            onTextSelected = {
                viewModel.onEvent(UIEvents.FirstName(it))
            },
            isError = signUpState.nameError,
            errorMessage = R.string.user_name_error
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Number",
            leadingIcon = Icons.Default.PhoneAndroid,
            onTextSelected = {
                viewModel.onEvent(UIEvents.Number(it))
            },
            isError = signUpState.numberError,
            errorMessage = R.string.number_error
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(
            labelValue = "Enter your Email",
            leadingIcon = Icons.Default.Email,
            onTextSelected = {
                viewModel.onEvent(UIEvents.Email(it))
            },
            isError = signUpState.emailError,
            errorMessage = R.string.email_error
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        PasswordTextFieldComponent(labelValue = "Enter your Password", onTextSelected = {
            viewModel.onEvent(UIEvents.Password(it))
        },
            isError = signUpState.passwordError,
            errorMessage = R.string.password_error
        )
        Spacer(modifier = Modifier.heightIn(30.dp))
        GradientButtonComponent(
            buttonText = R.string.sign_up,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {

            viewModel.onEvent(UIEvents.SignUpButtonClick)

        }
        Spacer(modifier = Modifier.heightIn(10.dp))
        BodySmallComponent(textValue = R.string.create_account) {
            navigateUpTo(navController, Screen.LoginScreen.route)
        }

    }

    if (viewModel.inProgress.value) {
        CommonProgressBar()
    }

}
