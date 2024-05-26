package com.example.jetpackcomposechatapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.navigation.navigateUpTo
import com.example.jetpackcomposechatapp.uiComponents.BodySmallComponent
import com.example.jetpackcomposechatapp.utils.Graph
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun ChatListScreen(
    navController: NavController
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BodySmallComponent(textValue = R.string.sign_out) {
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

        LetterByLetterAnimatedText()
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
    val text = "This text animates as though it is being typed \uD83E\uDDDE\u200D♀\uFE0F \uD83D\uDD10  \uD83D\uDC69\u200D❤\uFE0F\u200D\uD83D\uDC68 \uD83D\uDC74\uD83C\uDFFD"

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
    Text("${Firebase.auth.currentUser} ${Firebase.auth.currentUser?.displayName}")
}