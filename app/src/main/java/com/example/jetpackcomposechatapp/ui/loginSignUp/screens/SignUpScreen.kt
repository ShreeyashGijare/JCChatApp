package com.example.jetpackcomposechatapp.ui.loginSignUp.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.SignUpEvents
import com.example.jetpackcomposechatapp.ui.loginSignUp.viewmodel.SignUpViewModel
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ProfileEvents
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyExtraBold
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyMedium
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilySemiBold
import com.example.jetpackcomposechatapp.uiComponents.BlueBackgroundButtonComponent
import com.example.jetpackcomposechatapp.uiComponents.BodyLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.BodyMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.DisplayLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.HeadLineMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.OutlinedTextFieldComponent
import com.example.jetpackcomposechatapp.uiComponents.PasswordTextFieldComponent
import com.example.jetpackcomposechatapp.uiComponents.blueBackground
import com.example.jetpackcomposechatapp.utils.Graph
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun SignUpScreen(
    rootNavController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var currentDisplayedScreen by remember {
        mutableStateOf(SignUpScreens.ImageUploadScreen)
    }

    BackHandler {
        when (currentDisplayedScreen) {
            SignUpScreens.BasicDetailsScreen -> {
                rootNavController.navigateUp()
            }

            SignUpScreens.EmailPasswordScreen -> {
                viewModel.onEvent(SignUpEvents.Password(""))
                currentDisplayedScreen = SignUpScreens.BasicDetailsScreen
            }

            SignUpScreens.ImageUploadScreen -> {
                currentDisplayedScreen = SignUpScreens.EmailPasswordScreen
            }
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.3f)
                .blueBackground()
        )
        when (currentDisplayedScreen) {
            SignUpScreens.BasicDetailsScreen -> {
                BasicDetailsScreen(
                    nameValue = signUpState.name,
                    onNameChanged = {
                        viewModel.onEvent(SignUpEvents.Name(it))
                    },
                    isNameError = signUpState.nameError,
                    nameErrorMessage = signUpState.nameErrorMessage,
                    numberValue = signUpState.number,
                    onNumberChanged = {
                        viewModel.onEvent(SignUpEvents.Number(it))
                    },
                    isNumberError = signUpState.numberError,
                    numberErrorMessage = signUpState.numberErrorMessage,
                    onContinueClick = {
                        if (viewModel.validateName() && viewModel.validateNumber()) {
                            currentDisplayedScreen = SignUpScreens.EmailPasswordScreen
                        } else {
                            viewModel.validateName()
                            viewModel.validateNumber()
                        }
                    }
                )
            }

            SignUpScreens.EmailPasswordScreen -> {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                    exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                ) {
                    EmailPasswordScreen(
                        emailValue = signUpState.email,
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
                            if (viewModel.validateEmail() && viewModel.validatePassword()) {
//                                viewModel.onEvent(SignUpEvents.SignUpButtonClick)
                                currentDisplayedScreen = SignUpScreens.ImageUploadScreen
                            } else {
                                viewModel.validateEmail()
                                viewModel.validatePassword()
                            }
                        }
                    )
                }
            }

            SignUpScreens.ImageUploadScreen -> {
                ImageUploadScreen(
                    onUploadImage = { profileImageUri ->

                    }
                )
            }
        }
    }
}

@Composable
fun BasicDetailsScreen(
    nameValue: String,
    onNameChanged: (String) -> Unit,
    isNameError: Boolean,
    nameErrorMessage: String,
    numberValue: String,
    onNumberChanged: (String) -> Unit,
    isNumberError: Boolean,
    numberErrorMessage: String,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
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
            color = MaterialTheme.colorScheme.onPrimary
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
            text = nameValue,
            onTextSelected = {
                onNameChanged(it)
            },
            isError = isNameError,
            errorMessage = nameErrorMessage,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            )
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
            text = numberValue,
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
    emailValue: String,
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
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        DisplayLargeComponent(
            textValue = stringResource(id = R.string.app_name),
            fontFamily = interFontFamilyExtraBold,
            color = MaterialTheme.colorScheme.onPrimary
        )
        HeadLineMediumComponent(
            textValue = stringResource(R.string.app_slogan),
            fontFamily = interFontFamilySemiBold,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.fillMaxHeight(.1f))
        BodyMediumComponent(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            textValue = stringResource(R.string.enter_email_prompt),
            fontFamily = interFontFamilyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextFieldComponent(
            text = emailValue,
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
                onContinueClick.invoke()
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

@Composable
fun ImageUploadScreen(
    onUploadImage: (Uri) -> Unit
) {

    val context = LocalContext.current
    var imgUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            it?.let {
                imgUri = it
            }
            imgUri?.let { profileImageUri ->
                val outputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && profileImageUri.toString() != "") {
                    val src = ImageDecoder.createSource(context.contentResolver, profileImageUri)
                    val bitmap = ImageDecoder.decodeBitmap(src)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                }
                onUploadImage.invoke(profileImageUri)
            }
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.fillMaxHeight(.1f))

        BodyMediumComponent(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            textValue = stringResource(R.string.image_prompt),
            fontFamily = interFontFamilyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Transparent)
                .clickable {
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter =
                if (imgUri != null
                ) rememberImagePainter(data = imgUri) else painterResource(
                    id = R.drawable.ic_profile
                ), contentDescription = "",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )

            if (imgUri == null) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(6.dp),
                    tint = Color.White
                )
            }
        }

        if (imgUri != null) {
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedText(text = stringResource(R.string.image_success_prompt))
        }
    }
}


@Composable
fun AnimatedText(
    text: String
) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 50L

    var substringText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(text) {
        delay(500)
        breakIterator.text = StringCharacterIterator(text)
        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }
    BodyLargeComponent(
        textValue = substringText,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        fontFamily = interFontFamilySemiBold
    )

}

enum class SignUpScreens() {
    BasicDetailsScreen(),
    EmailPasswordScreen(),
    ImageUploadScreen()
}
