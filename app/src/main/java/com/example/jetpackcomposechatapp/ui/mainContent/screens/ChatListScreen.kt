package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist.ChatUserObject
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatListViewModel
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyBold
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilySemiBold
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
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
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(15.dp)
        ) {

            items(chatList) { userData ->
                UserChatItem(user = userData) {
                    homeNavController.navigate(
                        "${HomeRouteScreen.ChatScreen.route}?userData=${
                            Gson().toJson(
                                userData
                            )
                        }"
                    )
                }
            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(
    viewModel: ChatListViewModel,
    onClick: () -> Unit
) {

    val currentUserData by viewModel.currentUser.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = if (!currentUserData.imageUrl.isNullOrBlank()) rememberImagePainter(data = currentUserData.imageUrl) else painterResource(
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
            .padding(vertical = 5.dp)

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
            modifier = Modifier.padding(start = 15.dp)
        ) {
            LabelLargeComponent(
                textValue = user.name!!,
                color = MaterialTheme.colorScheme.onBackground
            )
            LabelSmallComponent(
                textValue = user.number!!,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
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
fun LetterByLetterAnimatedText() {
    val text =
        "This text animates as though it is being typed \uD83E\uDDDE\u200D♀\uFE0F \uD83D\uDD10  \uD83D\uDC69\u200D❤\uFE0F\u200D\uD83D\uDC68 \uD83D\uDC74\uD83C\uDFFD"

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