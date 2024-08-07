package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatEvents
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatViewModel
import com.example.jetpackcomposechatapp.uiComponents.BodyLargeComponent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    homeNavController: NavController,
    userData: UserData,
    viewModel: ChatViewModel = hiltViewModel()

) {

    val chatMessages by viewModel.chatMessages.collectAsState()
    var previousDate: LocalDate? = null

    LaunchedEffect(key1 = Unit) {
        viewModel.setReceiverUser(userData)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserChats()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        ChatScreenTopBar(
            onBackArrowClick = {
                homeNavController.popBackStack()
            },
            userData = userData
        )
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(15.dp)
        ) {

            items(chatMessages) { message ->
                Column {
                    val messageDate =
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(message.timeStamp),
                            ZoneId.systemDefault()
                        )
                            .toLocalDate()
                    if (messageDate != previousDate) {
                        previousDate = messageDate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = getFormattedDateLabel(message.timeStamp),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(vertical = 5.dp, horizontal = 8.dp),
                            )
                        }
                    } else {
                        previousDate = messageDate
                    }
                    MessageItem(message = message)
                }

//                MessageItem(message = message)
            }
        }
        ChatScreenBottomBar(
            onSendButtonClick = { message ->
                viewModel.sendMessage(message)
            }
        )
    }
}

@Composable
fun ChatScreenTopBar(
    onBackArrowClick: () -> Unit,
    userData: UserData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = stringResource(R.string.back_arrow),
            modifier = Modifier
                .weight(.2f)
                .clickable {
                    onBackArrowClick()
                }
        )
//        Spacer(modifier = Modifier.width(5.dp))
        Image(
            painter = if (!userData.imageUrl.isNullOrEmpty()) rememberImagePainter(data = userData.imageUrl) else painterResource(
                id = R.drawable.chat_icon_one
            ), contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(
                    CircleShape
                )
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                    RoundedCornerShape(50)
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        BodyLargeComponent(
            textValue = userData.name!!,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun MessageItem(message: ChatState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardColors(
                containerColor = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContentColor = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .widthIn(20.dp, 300.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(
                topStart = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 15.dp else 0.dp,
                topEnd = 15.dp,
                bottomEnd = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 0.dp else 15.dp,
                bottomStart = 15.dp
            )
        ) {
            Column(
                horizontalAlignment = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Alignment.End else Alignment.Start,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = message.message.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = convertLongToTimeAMPM(message.timeStamp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenBottomBar(
    onSendButtonClick: (String) -> Unit
) {

    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* handle attachment */ }) {
            Icon(
                imageVector = Icons.Filled.AttachFile,
                contentDescription = stringResource(R.string.attach)
            )
        }

        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(10.dp),
            placeholder = { Text(text = stringResource(R.string.type_a_message)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            ),
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onSendButtonClick(message)
                    message = ""
                    keyboardController?.hide()
                }
            )
        )

        IconButton(
            enabled = message.isNotBlank(),
            onClick = {
                onSendButtonClick(message)
                message = ""
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                tint = if (message.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
        }
    }
}

fun convertLongToTimeAMPM(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("hh:mm", Locale.getDefault())
    return format.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDateLabel(timeInMillis: Long): String {
    val dateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneId.systemDefault())
    val today = LocalDate.now()
    return when (dateTime.toLocalDate()) {
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }
}