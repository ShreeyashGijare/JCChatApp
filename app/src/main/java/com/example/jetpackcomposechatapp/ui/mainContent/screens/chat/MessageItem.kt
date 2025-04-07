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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import com.example.jetpackcomposechatapp.uiComponents.BodyLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.utils.DateUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    index: Int,
    message: ChatState,
    receiverUser: UserData,
    showReactions: Boolean,
    onLongClick: (Int) -> Unit,
    onReactionSelected: (String) -> Unit,
    onReactionRemoved: (Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
    ) {
        when (message.messageType) {
            MessageType.MESSAGE -> {


                val messageArrangement =
                    if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Alignment.BottomEnd else Alignment.BottomStart
                val messageColor =
                    if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary
                val reactionAlignment =
                    if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Alignment.BottomEnd else Alignment.BottomStart

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = messageArrangement
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                messageColor, shape = RoundedCornerShape(
                                    topStart = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 15.dp else 0.dp,
                                    topEnd = 15.dp,
                                    bottomEnd = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 0.dp else 15.dp,
                                    bottomStart = 15.dp
                                )
                            )
                            .combinedClickable(
                                onClick = {

                                },
                                onLongClick = {
                                    onLongClick.invoke(index)
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .widthIn(max = 280.dp)
                    ) {

                        Column {

                            /*Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Image(
                                    painter = if (!receiverUser.imageUrl.isNullOrBlank()) rememberImagePainter(
                                        data = receiverUser.imageUrl
                                    ) else painterResource(
                                        id = R.drawable.ic_profile
                                    ), contentDescription = "",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(
                                            CircleShape
                                        ),
                                    contentScale = ContentScale.FillBounds
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                LabelSmallComponent(
                                    textValue = receiverUser.name!!,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))*/
                            Text(
                                text = message.message.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }


                    }

                    if (message.messageReactions.isNotEmpty()) {
                        MessageReactions(
                            modifier = Modifier
                                .align(reactionAlignment)
                                .offset(
                                    y = 10.dp,
                                    x = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else (-10).dp
                                )
                                .background(Color.White)
                                .clip(RoundedCornerShape(10.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            reactions = message.messageReactions,
                            onReactionRemoved = {

                            }
                        )
                    }
                }


                /*Row(
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
                            .widthIn(100.dp, 300.dp)
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
                            Row(
                                modifier = Modifier
                                    .widthIn(100.dp, 300.dp)
                                    .wrapContentSize()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier
                                        .wrapContentWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = if (!receiverUser.imageUrl.isNullOrBlank()) rememberImagePainter(
                                            data = receiverUser.imageUrl
                                        ) else painterResource(
                                            id = R.drawable.ic_profile
                                        ), contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(
                                                CircleShape
                                            ),
                                        contentScale = ContentScale.FillBounds
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    LabelSmallComponent(
                                        textValue = receiverUser.name!!,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = DateUtils.convertLongToTimeAMPM(message.timeStamp),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = message.message.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
                MessageReactions(
                    reactions = message.messageReactions,
                    arrangement = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Arrangement.End else Arrangement.Start,
                    onReactionRemoved = {
                        onReactionRemoved.invoke(it)
                    }
                )*/
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
                                painter = if (!message.message.isNullOrBlank()) rememberAsyncImagePainter(
                                    model = message.message
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
                /*MessageReactions(
                    reactions = message.messageReactions,
                    arrangement = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Arrangement.End else Arrangement.Start,
                    onReactionRemoved = {
                        onReactionRemoved.invoke(it)
                    }
                )*/
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
fun MessageReactions(
    modifier: Modifier,
    reactions: List<String>,
    onReactionRemoved: (Int) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        reactions.forEachIndexed { index, emoji ->
            Text(
                text = emoji,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}


@Composable
fun MessageBubbleWithBottomOverlapReactions(
    message: String,
    reactions: List<String> = emptyList(),
    isUserMe: Boolean
) {
    val bubbleColor = if (isUserMe) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)
    val bubbleAlignment = if (isUserMe) Alignment.BottomEnd else Alignment.BottomStart
    val reactionAlignment = if (isUserMe) Alignment.BottomStart else Alignment.BottomEnd

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = bubbleAlignment
    ) {
        Box {
            // Message Bubble
            Box(
                modifier = Modifier
                    .background(bubbleColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .widthIn(max = 280.dp)
            ) {
                Text(
                    text = message,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            // Reactions overlapping bottom corner
            if (reactions.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(reactionAlignment)
                        .offset(y = 10.dp, x = if (isUserMe) 10.dp else (-10).dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEDEDED))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    reactions.forEach { reaction ->
                        Text(
                            text = reaction,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        }
    }
}