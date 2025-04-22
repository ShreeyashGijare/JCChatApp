package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist.ChatUserObject
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatListViewModel
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyBold
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilySemiBold
import com.example.jetpackcomposechatapp.uiComponents.BodyMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelMediumComponentSingleLine
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.uiComponents.SearchFieldComponent
import com.example.jetpackcomposechatapp.utils.DateUtils.formatMessageTimeStampToDate
import com.example.jetpackcomposechatapp.utils.Graph
import com.example.jetpackcomposechatapp.utils.HomeRouteScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.BreakIterator
import java.text.StringCharacterIterator
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(
    navController: NavController,
    homeNavController: NavController,
    viewModel: ChatListViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val chatList by viewModel.chatUserList.collectAsState()

    var isSearchClicked: Boolean by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserChats()
    }

    LaunchedEffect(key1 = viewModel.error) {
        viewModel.error.collectLatest {
            if (it.isError) {
                onShowSnackBar(it.errorMessage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .statusBarsPadding()
    ) {
        TopBar(
            isSearchClicked = isSearchClicked,
            viewModel = viewModel,
            onClick = {
                coroutineScope.launch {
                    if (signOutUser()) {
                        navController.popBackStack()
                        navigateUpTo(navController, Graph.AUTHENTICATION_GRAPH)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            onSearchIconCLicked = {
                isSearchClicked = it
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
            /*contentPadding = PaddingValues(vertical = 8.dp)*/
        ) {
            itemsIndexed(chatList) { index, userData ->
                UserChatItem(user = userData) {
                    homeNavController.navigate(
                        "${HomeRouteScreen.ChatScreen.route}?userData=${
                            Gson().toJson(
                                userData
                            )
                        }"
                    )/*isSearchClicked = false*/
                }
                if (index != chatList.indices.last) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onSecondaryContainer)
                            .padding(horizontal = 5.dp)
                    )
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(
    isSearchClicked: Boolean,
    viewModel: ChatListViewModel,
    onClick: () -> Unit,
    onSearchIconCLicked: (Boolean) -> Unit
) {

    val currentUserData by viewModel.currentUser.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(
            visible = isSearchClicked,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            SearchFieldComponent(
                labelValue = stringResource(id = R.string.hint_search),
                leadingIcon = Icons.Default.Search,
                onTextSelected = {

                },
                onCloseClick = {
                    onSearchIconCLicked.invoke(false)
                }
            )
        }

        AnimatedVisibility(
            visible = !isSearchClicked,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (!currentUserData.imageUrl.isNullOrBlank()) rememberAsyncImagePainter(
                        model = currentUserData.imageUrl
                    ) else painterResource(
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
                Column {
                    BodySmallComponent(
                        textValue = greetUserBasedOnTime(),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        fontFamily = interFontFamilySemiBold
                    ) {
                        onClick.invoke()
                    }
                    Text(
                        modifier = Modifier.width(180.dp),
                        text = currentUserData.name.toString(),
                        fontFamily = interFontFamilyBold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onSearchIconCLicked.invoke(true)
                    },
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {

                    },
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
}

@Composable
fun UserChatItem(
    user: ChatUserObject,
    onClick: (UserData) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val userData = UserData(
                    imageUrl = user.imageUrl,
                    name = user.name,
                    number = user.number,
                    userId = user.userId,
                    emailId = user.emailId
                )
                onClick.invoke(userData)
            }
            .padding(vertical = 8.dp, horizontal = 15.dp)

    ) {
        Image(
            painter = if (!user.imageUrl.isNullOrBlank()) rememberAsyncImagePainter(model = user.imageUrl) else painterResource(
                id = R.drawable.ic_profile
            ), contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(
                    CircleShape
                ),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(start = 15.dp)
                .weight(1f)
        ) {
            BodyMediumComponent(
                textValue = user.name!!,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = interFontFamilySemiBold
            )
            Spacer(modifier = Modifier.height(5.dp))

            when (user.lastMessageType) {
                MessageType.MESSAGE -> {
                    LabelMediumComponentSingleLine(
                        textValue = user.lastMessage,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                MessageType.IMAGE -> {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(width = 30.dp, height = 18.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_image),
                            contentDescription = "Image",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        LabelMediumComponentSingleLine(
                            textValue = "Photo",
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        //Add unread messages count here
        Spacer(modifier = Modifier.weight(.1f))
        LabelSmallComponent(textValue = formatMessageTimeStampToDate(user.timeStamp))
    }
}


suspend fun signOutUser(): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            Firebase.auth.signOut()
            true
        } catch (e: Exception) {
            false
        }
    }
}

@Composable
fun LetterByLetterAnimatedText(text: String) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 50L

    var substringText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(text) {
        delay(1000)
        breakIterator.text = StringCharacterIterator(text)

        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun greetUserBasedOnTime(): String {
    val currentTime = LocalTime.now()

    return when (currentTime.hour) {
        in 5..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        in 18..21 -> "Good Evening"
        else -> "Good Night"
    }
}
