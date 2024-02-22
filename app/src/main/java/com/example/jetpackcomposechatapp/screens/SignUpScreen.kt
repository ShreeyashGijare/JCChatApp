package com.example.jetpackcomposechatapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.uiComponents.GradientButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.HeadLineMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.OutlinedTextFieldComponent
import com.example.jetpackcomposechatapp.utils.Screen
import com.example.jetpackcomposechatapp.viewModel.ChatViewModel

@Preview
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
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
        OutlinedTextFieldComponent(labelValue = "Enter your Name")
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(labelValue = "Enter your Number")
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(labelValue = "Enter your Email")
        Spacer(modifier = Modifier.heightIn(10.dp))
        OutlinedTextFieldComponent(labelValue = "Enter your Password")
        Spacer(modifier = Modifier.heightIn(30.dp))
        GradientButtonComponent(
            buttonText = R.string.sign_up,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        Spacer(modifier = Modifier.heightIn(10.dp))
        BodySmallComponent(textValue = R.string.create_account) {
            navigateUpTo(navController, Screen.LoginScreen.route)
        }

    }
}
