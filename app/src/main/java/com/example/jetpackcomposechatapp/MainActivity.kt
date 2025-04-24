package com.example.jetpackcomposechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.jetpackcomposechatapp.navigation.graphs.RootNavGraph
import com.example.jetpackcomposechatapp.ui.theme.JetPackComposeChatAppTheme
import com.example.jetpackcomposechatapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var fbMessaging: FirebaseMessaging

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetPackComposeChatAppTheme {


                firebaseAuth.currentUser?.let { user ->
                    fbMessaging.token.addOnCompleteListener {
                        if (it.isSuccessful) {
                            db.collection(Constants.USER_NODE).document(user.uid).update("token", it.result)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    RootNavGraph()
                }

            }
        }
    }
}
