package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatViewModel
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilySemiBold
import com.example.jetpackcomposechatapp.uiComponents.BodyLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.utils.DateUtils.convertLongToTimeAMPM
import com.example.jetpackcomposechatapp.utils.DateUtils.formatDate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
    val receiverUser by viewModel.receiverUser.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.setReceiverUser(userData)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .statusBarsPadding()
    ) {

        ChatScreenTopBar(
            onBackArrowClick = {
                homeNavController.popBackStack()
            },
            userData = receiverUser
        )
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(15.dp)
        ) {
            items(groupMessagesByDay(chatMessages).reversed()) { dayMessage ->
                when (dayMessage) {
                    is DayMessage.Header -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LabelSmallComponent(
                                textValue = dayMessage.date,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(MaterialTheme.colorScheme.onPrimary)
                                    .padding(vertical = 5.dp, horizontal = 8.dp),
                                fontFamily = interFontFamilySemiBold
                            )
                        }
                    }

                    is DayMessage.Item -> {
                        MessageItem(message = dayMessage.message)
                    }
                }
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
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(.2f)
                .clickable {
                    onBackArrowClick()
                }
        )
        Image(
            painter = if (!userData.imageUrl.isNullOrBlank()) rememberImagePainter(data = userData.imageUrl) else painterResource(
                id = R.drawable.ic_profile
            ), contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(
                    CircleShape
                ),
            contentScale = ContentScale.FillBounds
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
                containerColor = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary,
                contentColor = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
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

@Composable
fun ChatScreenBottomBar(
    onSendButtonClick: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
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

fun groupMessagesByDay(messages: List<ChatState>): List<DayMessage> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return messages.groupBy {
        dateFormat.format(Date(it.timeStamp))
    }.flatMap { (date, messagesOnDate) ->
        val dateObject = dateFormat.parse(date)
        val formattedDate = formatDate(dateObject!!)
        val dayHeader = DayMessage.Header(formattedDate)
        val messageItems = messagesOnDate.map { DayMessage.Item(it) }
        listOf(dayHeader) + messageItems
    }
}

sealed class DayMessage {
    data class Header(val date: String) : DayMessage()
    data class Item(val message: ChatState) : DayMessage()
}