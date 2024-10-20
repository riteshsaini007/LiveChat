
package com.example.livechat.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonImage
import com.example.livechat.LCViewModel
import com.example.livechat.data.Message
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.unit.sp

@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, chatId: String) {

    var reply by rememberSaveable {
        mutableStateOf("")
    }

    val onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }

    var chatMessage = vm.chatMessages

    val myUser = vm.userData.value
    var currentChat = vm.chats.value.first { it.chatId == chatId }
    val chatUser = if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

    LaunchedEffect(key1 = Unit) {
        vm.populateMessage(chatId)
    }

    BackHandler {
        vm.depopulateMessage()
        navController.popBackStack()
    }

    // Apply imePadding to adjust the layout when the keyboard is open
    Column(
        modifier = Modifier
            .fillMaxSize()
           // .background(Color(0xFFB22222))
            .background(Color(0xFF800000))
            //.background(Color.LightGray) // Light gray background for chat screen
            .imePadding()
    ) {
        ChatHeader(
            name = chatUser.name ?: "",
            imageUrl = chatUser.imageUrl ?: "",
            onBackClicked = {
                navController.popBackStack()
                vm.depopulateMessage()
            }
        )
        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessages = chatMessage.value,
            currentUserId = myUser?.userId ?: ""
        )
        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
    }
}


@Composable
fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {

    // State to control scrolling
    val listState = rememberLazyListState()

    // Scroll to the last message when new messages are added
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    LazyColumn(
        state = listState,  // Attach the scroll state to the LazyColumn
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    ) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color(0xff68c400) else Color(0xffc0c0c0)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = alignment
            ) {
                // Rounded corners for message bubbles
                Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))  // Increased curve for smoother edges
                        .background(color)
                        .padding(12.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun ChatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.DarkGray, shape = RoundedCornerShape(16.dp)) // Dark gray background with curved edges
            .padding(4.dp), // Increased padding for better spacing
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null,
            tint = Color.White, // White arrow icon for better visibility
            modifier = Modifier
                .clip(CircleShape) // Rounded back button
                .clickable { onBackClicked.invoke() }
                .padding(8.dp)
        )
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape) // Circular user image
        )
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            color = Color.White, // White text for better contrast
            fontSize = 18.sp, // Slightly larger font for better readability
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = reply,
                onValueChange = onReplyChange,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))  // Curved edges for text input
                    .background(Color.White),  // White background for the text field
                maxLines = 3
            )
            Button(
                onClick = onSendReply,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(16.dp))  // Curved edges for the button
            ) {
                Text(text = "Send")
            }
        }
    }
}
