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
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.utils.DateUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

enum class SwipeToReply {
    Resting, Replying
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    index: Int,
    message: ChatState,
    receiverUser: UserData,
    showReactions: Boolean,
    onLongClick: (Int) -> Unit,
    onReactionSelected: (String) -> Unit,
    onReactionRemoved: (Int) -> Unit,
    onReply: (ChatState) -> Unit
) {

    val density = LocalDensity.current
    val messageOverallScroll = rememberOverscrollEffect()
    val messageDragState = remember {
        AnchoredDraggableState(
            initialValue = SwipeToReply.Resting
        )
    }
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val swipeThreshold = 48.dp

    val anchors = remember(density, isCurrentUser) {
        val replyOffset = with(density) {
            if (isCurrentUser) -swipeThreshold.toPx() else swipeThreshold.toPx()
        }
        DraggableAnchors {
            SwipeToReply.Resting at if (isCurrentUser) 1f else 0f
            SwipeToReply.Replying at replyOffset
        }
    }
    SideEffect {
        messageDragState.updateAnchors(anchors)
    }

    LaunchedEffect(key1 = messageDragState) {
        snapshotFlow { messageDragState.settledValue }
            .collectLatest {
                if (it == SwipeToReply.Replying) {
                    delay(300)
                    messageDragState.animateTo(SwipeToReply.Resting)
                    onReply.invoke(message)
                }
            }
    }


    val messageArrangement =
        if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Alignment.BottomEnd else Alignment.BottomStart
    val messageColor =
        if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary
    val reactionAlignment =
        if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) Alignment.BottomEnd else Alignment.BottomStart

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(bottom = if (message.messageReactions.isNotEmpty()) 8.dp else 0.dp)
    ) {
        when (message.messageType) {
            MessageType.MESSAGE -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .anchoredDraggable(
                            messageDragState,
                            Orientation.Horizontal,
                            overscrollEffect = messageOverallScroll,
                            enabled = true,

                            )
                        .overscroll(messageOverallScroll)
                        .offset {
                            IntOffset(
                                x = messageDragState
                                    .requireOffset()
                                    .roundToInt(),
                                y = 0
                            )
                        }

                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = messageArrangement
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                messageColor, shape = RoundedCornerShape(
                                    topStart = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else 0.dp,
                                    topEnd = 10.dp,
                                    bottomEnd = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 0.dp else 10.dp,
                                    bottomStart = 10.dp
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
                            .widthIn(min = 50.dp, max = 280.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .widthIn(min = 50.dp, max = 280.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = if (!receiverUser.imageUrl.isNullOrBlank()) rememberAsyncImagePainter(
                                        model = receiverUser.imageUrl
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
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = DateUtils.convertLongToTimeAMPM(message.timeStamp),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
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
                                    y = 20.dp,
                                    x = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else (-10).dp
                                )
                                .background(Color.White, RoundedCornerShape(15.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            reactions = message.messageReactions,
                            onReactionRemoved = { reactionIndex ->
                                onReactionRemoved.invoke(reactionIndex)
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = showReactions,
                        enter = fadeIn() + expandIn(),
                        exit = fadeOut() + shrinkOut()
                    ) {
                        ReactionPicker(
                            modifier = Modifier
                                .align(reactionAlignment)
                                .offset(
                                    y = 10.dp,
                                    x = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else (-10).dp
                                ),
                        ) { selectedReaction ->
                            onReactionSelected.invoke(selectedReaction)
                            onLongClick.invoke(-1)
                        }
                    }
                }
            }

            MessageType.IMAGE -> {

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
                                    topStart = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else 0.dp,
                                    topEnd = 10.dp,
                                    bottomEnd = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 0.dp else 10.dp,
                                    bottomStart = 10.dp
                                )
                            )
                            .combinedClickable(
                                onClick = {

                                },
                                onLongClick = {
                                    onLongClick.invoke(index)
                                }
                            )
                            /*.padding(horizontal = 12.dp, vertical = 8.dp)*/
                            .widthIn(min = 50.dp, max = 280.dp)
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
                    if (message.messageReactions.isNotEmpty()) {
                        MessageReactions(
                            modifier = Modifier
                                .align(reactionAlignment)
                                .offset(
                                    y = 20.dp,
                                    x = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else (-10).dp
                                )
                                .background(Color.White, RoundedCornerShape(15.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            reactions = message.messageReactions,
                            onReactionRemoved = { reactionIndex ->
                                onReactionRemoved.invoke(reactionIndex)
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = showReactions,
                        enter = fadeIn() + expandIn(),
                        exit = fadeOut() + shrinkOut()
                    ) {
                        ReactionPicker(
                            modifier = Modifier
                                .align(reactionAlignment)
                                .offset(
                                    y = 10.dp,
                                    x = if (message.senderId.equals(Firebase.auth.currentUser!!.uid)) 10.dp else (-10).dp
                                ),
                        ) { selectedReaction ->
                            onReactionSelected.invoke(selectedReaction)
                            onLongClick.invoke(-1)
                        }
                    }
                }
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
                fontSize = 18.sp,
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
                modifier = Modifier
                    .clickable {
                        onReactionRemoved.invoke(index)
                    }
                    .padding(end = 4.dp)
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
                    .widthIn(min = 50.dp, max = 280.dp)
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