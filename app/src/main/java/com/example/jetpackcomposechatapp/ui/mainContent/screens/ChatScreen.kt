package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import com.example.jetpackcomposechatapp.ui.mainContent.screens.chat.MessageItem
import com.example.jetpackcomposechatapp.ui.mainContent.screens.chat.ReactionPicker
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatEvents
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatViewModel
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilySemiBold
import com.example.jetpackcomposechatapp.uiComponents.BodyLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.utils.CameraUtils.getUriFromBitmap
import com.example.jetpackcomposechatapp.utils.DateUtils.DATE_FORMAT
import com.example.jetpackcomposechatapp.utils.DateUtils.formatDate
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    homeNavController: NavController, userData: UserData, viewModel: ChatViewModel = hiltViewModel()
) {
    val chatMessages by viewModel.chatMessages.collectAsState()
    val receiverUser by viewModel.receiverUser.collectAsState()

    //Send image permission, capture image functionality starts here
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Uri?>(null) }

    val notLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){}
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Camera intent launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageBitmap = getUriFromBitmap(result.data?.extras?.get("data") as Bitmap, context)
            imageBitmap?.let { imageUri ->
                val outputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && imageUri.toString() != "") {
                    val src = ImageDecoder.createSource(context.contentResolver, imageUri)
                    val bitmap = ImageDecoder.decodeBitmap(src)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
                }
                viewModel.onEvents(ChatEvents.UploadImage(imageUri, MessageType.IMAGE))
            }
        }
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    //Send image permission, capture image functionality ends here

    //Used to show reaction picker now
    var currentSelectedMessageIndex: Int by remember {
        mutableIntStateOf(-1)
    }

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
            }, userData = receiverUser
        )
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    detectTapGestures { currentSelectedMessageIndex = -1 } // Dismiss on outside tap
                }, contentPadding = PaddingValues(15.dp)
        ) {
            itemsIndexed(groupMessagesByDay(chatMessages).reversed()) { index, dayMessage ->
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
                        MessageItem(index = index,
                            message = dayMessage.message,
                            receiverUser = receiverUser,
                            showReactions = index == currentSelectedMessageIndex,
                            onLongClick = {
                                currentSelectedMessageIndex = it
                            },
                            onReactionSelected = { selectedReaction ->
                                viewModel.onEvents(
                                    ChatEvents.AddReaction(
                                        messageId = dayMessage.message.messageId,
                                        reaction = selectedReaction
                                    )
                                )
                            },
                            onReactionRemoved = { reactionIndex ->
                                viewModel.onEvents(
                                    ChatEvents.RemoveReaction(
                                        messageId = dayMessage.message.messageId,
                                        reaction = dayMessage.message.messageReactions[reactionIndex]
                                    )
                                )
                            })
                    }
                }
            }

        }
        ChatScreenBottomBar(homeNavController = homeNavController, onSendButtonClick = { message ->
            viewModel.onEvents(ChatEvents.Message(message, MessageType.MESSAGE))
        }, onSendImageClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                }

                else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        })
    }
}

@Composable
fun ChatScreenTopBar(
    onBackArrowClick: () -> Unit, userData: UserData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(.2f)
                .clickable {
                    onBackArrowClick()
                })
        Image(
            painter = if (!userData.imageUrl.isNullOrBlank()) rememberImagePainter(data = userData.imageUrl) else painterResource(
                id = R.drawable.ic_profile
            ), contentDescription = "", modifier = Modifier
                .size(45.dp)
                .clip(
                    CircleShape
                ), contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.width(10.dp))
        BodyLargeComponent(
            textValue = userData.name!!, color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun ChatScreenBottomBar(
    homeNavController: NavController,
    onSendButtonClick: (String) -> Unit,
    onSendImageClick: () -> Unit
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
                imageVector = ImageVector.vectorResource(R.drawable.ic_attach),
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
            shape = RoundedCornerShape(20.dp),
            placeholder = { Text(text = stringResource(R.string.type_a_message)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onSend = {
                onSendButtonClick(message)
                message = ""
                keyboardController?.hide()
            })
        )

        IconButton(enabled = message.isNotBlank(), onClick = {
            onSendButtonClick(message)
            message = ""
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_send_message),
                contentDescription = "Send",
                tint = if (message.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
            )
        }

        IconButton(onClick = {
            onSendImageClick.invoke()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Image",
                tint = Color.Unspecified
            )
        }
    }
}

fun groupMessagesByDay(messages: List<ChatState>): List<DayMessage> {
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
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

@Composable
fun CaptureImageScreen(
    onSendImageClick: (Uri) -> Unit
) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Uri?>(null) }

    // Camera intent launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            imageBitmap = getUriFromBitmap(result.data?.extras?.get("data") as Bitmap, context)

            imageBitmap?.let { imageUri ->
                val outputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && imageUri.toString() != "") {
                    val src = ImageDecoder.createSource(context.contentResolver, imageUri)
                    val bitmap = ImageDecoder.decodeBitmap(src)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                }
            }
        }
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(cameraIntent)
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        imageBitmap?.let { image ->
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(enabled = imageBitmap != null, onClick = {
            imageBitmap?.let {
                onSendImageClick.invoke(it)
            }
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_send_message),
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeForStickyHeader() {
    LazyColumn {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                LabelSmallComponent(
                    textValue = "dayMessage.date",
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(vertical = 50.dp, horizontal = 80.dp),
                    fontFamily = interFontFamilySemiBold
                )
            }
        }

        stickyHeader {
            ReactionPicker(modifier = Modifier) {

            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                LabelSmallComponent(
                    textValue = "dayMessage.date",
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(vertical = 50.dp, horizontal = 80.dp),
                    fontFamily = interFontFamilySemiBold
                )
            }
        }
        stickyHeader {
            ReactionPicker(modifier = Modifier) {

            }
        }
    }

    /*val groupedMessages = groupMessagesByDay(chatMessages).reversed()
    var currentIndex = 0
    groupedMessages.forEach { dayMessage ->

        if (dayMessage is DayMessage.Header) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
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
        } else if (dayMessage is DayMessage.Item) {
            item {
                MessageItem(
                    index = currentIndex,
                    message = dayMessage.message,
                    receiverUser = receiverUser,
                    showReactions = currentIndex == currentSelectedMessageIndex,
                    onLongClick = {
                        currentSelectedMessageIndex = it
                    },
                    onReactionSelected = { selectedReaction ->
                        viewModel.onEvents(
                            ChatEvents.AddReaction(
                                messageId = dayMessage.message.messageId,
                                reaction = selectedReaction
                            )
                        )
                    },
                    onReactionRemoved = { reactionIndex ->
                        viewModel.onEvents(
                            ChatEvents.RemoveReaction(
                                messageId = dayMessage.message.messageId,
                                reaction = dayMessage.message.messageReactions[reactionIndex]
                            )
                        )
                    }
                )
            }
            currentIndex++
        }
    }*/
}