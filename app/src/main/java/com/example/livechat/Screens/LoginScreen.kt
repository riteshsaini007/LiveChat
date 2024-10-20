/*
package com.example.livechat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.livechat.CheckSignedIn
import com.example.livechat.CommonProgressBar
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.R
import com.example.livechat.navigateTo

@Composable
fun LoginScreen(vm: LCViewModel, navController: NavController) {


    CheckSignedIn(vm = vm, navController = navController)


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }

            val focus = LocalFocusManager.current

            Image(
                painter = painterResource(id = R.drawable.odurbk0), contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Sign In",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = emailState.value, onValueChange = {
                    emailState.value = it
                },
                label = { Text(text = "E-mail") },
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = passwordState.value, onValueChange = {
                    passwordState.value = it
                },
                label = { Text(text = "Password") },
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {

                    vm.loginIn(emailState.value.text,passwordState.value.text)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "SING IN")

            }

            Text(text = "New User ? Go to Sign Up ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp.route)
                    })
        }
    }
    if (vm.inProcess.value) {
        CommonProgressBar()
    }


}*/


package com.example.livechat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.livechat.CheckSignedIn
import com.example.livechat.CommonProgressBar
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.R
import com.example.livechat.navigateTo

@Composable
fun LoginScreen(vm: LCViewModel, navController: NavController) {

    CheckSignedIn(vm = vm, navController = navController)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Add padding around the screen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()), // Make the screen scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // State for email and password fields
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }

            val focus = LocalFocusManager.current

            // Circular Image with a green border
            Image(
                painter = painterResource(id = R.drawable.odurbk0),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp) // Set image size
                    .clip(CircleShape) // Make the image circular
                    .border(3.dp, Color(0xFF00FF00), CircleShape) // Green border
                    .padding(8.dp)
            )

            // Title "Sign In"
            Text(
                text = "Sign In",
                fontSize = 32.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5), // Vibrant color for title
                modifier = Modifier.padding(8.dp)
            )

            // Email Input Field with rounded corners
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(text = "E-mail") },
                shape = RoundedCornerShape(12.dp), // Rounded corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Password Input Field with rounded corners
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(text = "Password") },
                shape = RoundedCornerShape(12.dp), // Rounded corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Sign In Button with rounded corners
            Button(
                onClick = {
                    vm.loginIn(emailState.value.text, passwordState.value.text)
                },
                shape = RoundedCornerShape(16.dp), // Rounded button corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp) // Set fixed height for consistent appearance
            ) {
                Text(text = "SIGN IN", fontSize = 18.sp)
            }

            // Navigation Text for Sign Up
            Text(
                text = "New User? Go to Sign Up ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp.route)
                    }
            )
        }
    }

    // Show progress bar when in process
    if (vm.inProcess.value) {
        CommonProgressBar()
    }
}
