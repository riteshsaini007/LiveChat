/*
package com.example.livechat.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonProgressBar
import com.example.livechat.CommonRow
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.TitleText
import com.example.livechat.navigateTo



@Composable
fun StatusScreen(navController: NavController, vm: LCViewModel) {

    val inProcess = vm.inProgressStatus.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        val statuses = vm.status.value
        val userData = vm.userData.value
        val myStatuses = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatuses = statuses.filter {
            it.user.userId != userData?.userId
        }

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    vm.uploadStatus(uri)
                }
            }

        Scaffold(
            floatingActionButton = {
                FAB {
                    launcher.launch("image/*")
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TitleText(txt = "Status")  // Make sure TitleText is aligned to the top
                    if (statuses.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No Statuses available")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            if (myStatuses.isNotEmpty()) {
                                item {
                                    CommonRow(
                                        imageUrl = myStatuses[0].user.imageUrl,
                                        name = myStatuses[0].user.name
                                    ) {
                                        navigateTo(
                                            navController = navController,
                                            DestinationScreen.SingleStatus.createRoute(myStatuses[0].user.userId!!)
                                        )
                                    }
                                    CommonDivider()
                                }
                            }

                            val uniqueUsers = otherStatuses.map { it.user }.toSet().toList()
                            items(uniqueUsers) { user ->
                                CommonRow(imageUrl = user.imageUrl, name = user.name) {
                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.SingleStatus.createRoute(user.userId!!)
                                    )
                                }
                            }
                        }
                    }

                    BottomNavigationMenu(
                        selectedItem = BottomNavigationItem.STATUSLIST,
                        navController = navController
                    )
                }
            }
        )
    }
}

@Composable
fun FAB(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier
            .padding(bottom = 72.dp, end = 16.dp)
            //.padding(16.dp)  // Adjust padding properly
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = "Add Status",
            tint = Color.White
        )
    }
}
*/


 */

package com.example.livechat.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight // Make sure this is imported
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.livechat.*

@Composable
fun StatusScreen(navController: NavController, vm: LCViewModel) {

    val inProcess = vm.inProgressStatus.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        val statuses = vm.status.value
        val userData = vm.userData.value
        val myStatuses = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatuses = statuses.filter {
            it.user.userId != userData?.userId
        }

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    vm.uploadStatus(uri)
                }
            }

        Scaffold(
            floatingActionButton = {
                FAB {
                    launcher.launch("image/*")
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Top Title Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 33.sp,
                                fontWeight = FontWeight.Bold // Make the title bold
                            )
                        )
                        /*Text(
                            text = "Status",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold // Bold font for "Status"
                            )
                        )*/
                    }

                    if (statuses.isEmpty()) {
                        // Show if no status available
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No Statuses available",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            if (myStatuses.isNotEmpty()) {
                                item {
                                    CommonRow(
                                        imageUrl = myStatuses[0].user.imageUrl,
                                        name = myStatuses[0].user.name
                                    ) {
                                        navigateTo(
                                            navController = navController,
                                            DestinationScreen.SingleStatus.createRoute(myStatuses[0].user.userId!!)
                                        )
                                    }
                                    CommonDivider()
                                }
                            }

                            val uniqueUsers = otherStatuses.map { it.user }.toSet().toList()
                            items(uniqueUsers) { user ->
                                CommonRow(imageUrl = user.imageUrl, name = user.name) {
                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.SingleStatus.createRoute(user.userId!!)
                                    )
                                }
                                CommonDivider()
                            }
                        }
                    }

                    // Bottom Navigation Bar
                    BottomNavigationMenu(
                        selectedItem = BottomNavigationItem.STATUSLIST,
                        navController = navController
                    )
                }
            }
        )
    }
}

@Composable
fun FAB(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        modifier = Modifier
            .padding(bottom = 72.dp, end = 16.dp)
            .size(56.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = "Add Status",
            tint = Color.White
        )
    }
}

@Composable
fun CommonRow(
    imageUrl: String?,
    name: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonImage(data = imageUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, style = MaterialTheme.typography.bodyMedium)
    }
}
