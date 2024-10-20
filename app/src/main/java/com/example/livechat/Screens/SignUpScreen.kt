/*
package com.example.livechat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    vm: LCViewModel
) {


    CheckSignedIn(vm = vm, navController = navController )


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val nameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val numberState = remember {
                mutableStateOf(TextFieldValue())
            }
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
                text = "Sign Up",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(value = nameState.value, onValueChange = {
                nameState.value = it
            },
                label = {Text(text = "Name")},
                modifier = Modifier.padding(8.dp)
                )

            OutlinedTextField(value = numberState.value, onValueChange = {
                numberState.value = it
            },
                label = {Text(text = "Number")},
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(value = emailState.value, onValueChange = {
                emailState.value = it
            },
                label = {Text(text = "E-mail")},
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(value = passwordState.value, onValueChange = {
                 passwordState.value = it
            },
                label = {Text(text = "Password")},
                modifier = Modifier.padding(8.dp)
            )

            Button(onClick = { vm.signup(
               name =  nameState.value.text,
               number = numberState.value.text,
               email =  emailState.value.text,
               password =  passwordState.value.text
            )},
                modifier = Modifier.padding(8.dp)) {
                Text(text = "SING UP")
                
            }

            Text(text = "Already a user ? Go to login ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    })


        }
    }
    if(vm.inProcess.value){
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
import androidx.compose.material3.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    vm: LCViewModel
) {
    CheckSignedIn(vm = vm, navController = navController)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp) // Add padding to the screen for a cleaner layout
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()), // Make the screen scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Image with circular shape and green border
            Image(
                painter = painterResource(id = R.drawable.odurbk0),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp) // Set size of the image
                    .clip(CircleShape) // Make the image circular
                    .border(1.dp, Color(0xFF00FF00), CircleShape) // Green outline
                    .padding(8.dp)
            )

            // Title text
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5), // Use a vibrant color for the title
                modifier = Modifier.padding(8.dp)
            )

            // Text fields for user inputs with rounded corners and enhanced padding
            val nameState = remember { mutableStateOf(TextFieldValue()) }
            val numberState = remember { mutableStateOf(TextFieldValue()) }
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }

            val focus = LocalFocusManager.current

            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text(text = "Name") },
                shape = RoundedCornerShape(10.dp), // Rounded corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )

            OutlinedTextField(
                value = numberState.value,
                onValueChange = { numberState.value = it },
                label = { Text(text = "Number") },
                shape = RoundedCornerShape(12.dp), // Rounded corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(text = "E-mail") },
                shape = RoundedCornerShape(12.dp), // Rounded corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(text = "Password") },
                shape = RoundedCornerShape(12.dp), // Rounded corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )

            // Button for sign up
            Button(
                onClick = {
                    vm.signup(
                        name = nameState.value.text,
                        number = numberState.value.text,
                        email = emailState.value.text,
                        password = passwordState.value.text
                    )
                },
                shape = RoundedCornerShape(16.dp), // Rounded button corners
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp) // Add padding around the button
                    .height(50.dp) // Set a fixed height for a more consistent look
            ) {
                Text(text = "SIGN UP", fontSize = 18.sp)
            }

            // Text for navigation to the login screen
            Text(
                text = "Already a user? Go to login ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    }
            )
        }
    }

    // Display progress bar if in process
    if (vm.inProcess.value) {
        CommonProgressBar()
    }
}
