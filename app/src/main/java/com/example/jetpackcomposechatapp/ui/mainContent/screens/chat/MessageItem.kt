package com.example.jetpackcomposechatapp.ui.mainContent.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import com.example.jetpackcomposechatapp.utils.DateUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    index: Int,
    message: ChatState,
    showReactions: Boolean,
    onLongClick: (Int) -> Unit,
    onReactionSelected: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
    ) {
        when (message.messageType) {
            MessageType.MESSAGE -> {
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
                            .combinedClickable(
                                onClick = {

                                },
                                onLongClick = {
                                    onLongClick.invoke(index)
                                }
                            )
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
                                text = DateUtils.convertLongToTimeAMPM(message.timeStamp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                MessageReactions(
                    reactions = message.messageReactions,
                    arrangement = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Arrangement.End else Arrangement.Start
                )
            }

            MessageType.IMAGE -> {
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
                            .combinedClickable(
                                onClick = {

                                },
                                onLongClick = {
                                    onLongClick.invoke(index)
                                }
                            )
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
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                        ) {
                            Image(
                                painter = if (!message.message.isNullOrBlank()) rememberImagePainter(
                                    data = message.message
                                ) else painterResource(
                                    id = R.drawable.ic_profile
                                ), contentDescription = "",
                                modifier = Modifier
                                    .size(width = 250.dp, height = 350.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.FillBounds
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = DateUtils.convertLongToTimeAMPM(message.timeStamp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                MessageReactions(
                    reactions = message.messageReactions,
                    arrangement = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Arrangement.End else Arrangement.Start
                )
            }
        }

        AnimatedVisibility(
            visible = showReactions,
            enter = fadeIn() + expandIn(),
            exit = fadeOut() + shrinkOut()
        ) {
            ReactionPicker(
                modifier = Modifier,
            ) { selectedReaction ->
                onReactionSelected.invoke(selectedReaction)
                onLongClick.invoke(-1)
            }
        }
    }
}

@Composable
fun ReactionPicker(
    modifier: Modifier,
    onReactionSelected: (String) -> Unit
) {
    val reactions = listOf("❤️", "😂", "👍", "😲", "😢", "🔥")
    Row(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(8.dp)
    ) {
        reactions.forEach { emoji ->
            Text(
                text = emoji,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onReactionSelected(emoji) }
            )
        }
    }
}


@Composable
fun MessageReactions(arrangement: Arrangement.Horizontal, reactions: List<String>) {
    Row(
        horizontalArrangement = arrangement,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(2.dp)
        ) {
            reactions.forEach { emoji ->
                Text(
                    text = emoji,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }


}