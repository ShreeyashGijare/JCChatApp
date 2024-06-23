package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ChatListViewModel
import com.example.jetpackcomposechatapp.ui.theme.interFontFamilyExtraBold
import com.example.jetpackcomposechatapp.uiComponents.HeadLineLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.utils.Graph
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun ChatListScreen(
    navController: NavController,
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


    Column {
        TopBar(
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
                .weight(1f),
            contentPadding = PaddingValues(15.dp)
        ) {

            items(chatList) { user ->
                UserChatItem(user = user)
            }

        }
    }

}

@Composable
fun TopBar(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeadLineLargeComponent(
                modifier = Modifier.clickable {
                    onClick()
                },
                textValue = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = interFontFamilyExtraBold
            )
        }
    }

}

@Composable
fun UserChatItem(
    user: UserData
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            }
            .padding(vertical = 5.dp)

    ) {
        Image(
            painter = if (user.imageUrl != null) rememberImagePainter(data = user.imageUrl) else painterResource(
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