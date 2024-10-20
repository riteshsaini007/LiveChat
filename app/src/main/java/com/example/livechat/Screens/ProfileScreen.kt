
package com.example.livechat.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonImage
import com.example.livechat.CommonProgressBar
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.navigateTo

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Profile content with padding and scrolling
            ProfileContent(
                modifier = Modifier
                    .weight(1f)  // Occupy available space
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                vm = vm,
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onSave = {
                    vm.createOrUpdateProfile(name = name, number = number)
                },
                onBack = {
                    navigateTo(
                        navController = navController,
                        route = DestinationScreen.ChatList.route
                    )
                },
                onLogOut = {
                    vm.logout()
                    navigateTo(navController = navController, route = DestinationScreen.Login.route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogOut: () -> Unit
) {
    val imageUrl = vm.userData.value?.imageUrl

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)  // Spacing between components
    ) {
        // Top bar with Back and Save buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Back",
                modifier = Modifier
                    .clickable { onBack.invoke() }
                    .padding(8.dp),
                color = Color.Blue
            )
            Text(
                text = "Save",
                modifier = Modifier
                    .clickable { onSave.invoke() }
                    .padding(8.dp),
                color = Color.Green
            )
        }

        CommonDivider()

        // Profile image with border and shadow
        ProfileImage(imageUrl = imageUrl, vm = vm)

        CommonDivider()

        // Name input field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.Black,
                    containerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(56.dp)
            )
        }

        // Number input field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(
                value = number,
                onValueChange = onNumberChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.Black,
                    containerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(56.dp)
            )
        }

        CommonDivider()

        // Logout button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LogOut",
                modifier = Modifier
                    .clickable { onLogOut.invoke() }
                    .padding(16.dp)
                    .background(Color.Red, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                color = Color.White
            )
        }
    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile image with border and shadow
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(120.dp),
                //elevation = 4.dp
            ) {
                CommonImage(data = imageUrl, modifier = Modifier.fillMaxSize())
            }
            Text(text = "Change Profile Picture", color = Color.Gray)
        }

        val isLoading = vm.inProcess.value
        if (isLoading) {
            CommonProgressBar()
        }
    }
}
