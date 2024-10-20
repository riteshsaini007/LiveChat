/*
package com.example.livechat.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonImage
import com.example.livechat.LCViewModel
import com.example.livechat.data.Status


enum class State {
    INITIAL, ACTIVE, COMPLETED
}

@Composable
fun SingleStatusScreen(navController: NavController, vm: LCViewModel, userId: String) {
    val statuses = vm.status.value.filter {
        it.user.userId == userId
    }
    if (statuses.isNotEmpty()) {
        val currentStatus = remember {
            mutableStateOf(0)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            CommonImage(
                data = statuses[currentStatus.value].imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                statuses.forEachIndexed { index, status ->
                    CustomProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .height(7.dp)
                            .padding(1.dp),
                        state = if (currentStatus.value < index) State.INITIAL else if (currentStatus.value == index) State.ACTIVE else State.COMPLETED
                    ) {

                        if(currentStatus.value< statuses.size -1 ) currentStatus.value++ else navController.popBackStack()
                    }
                }

            }

        }
    }


}


@Composable
fun CustomProgressIndicator(modifier: Modifier, state: State, onComplete: () -> Unit) {

    var proress = if (state == State.INITIAL) 0f else 1f
    if (state == State.ACTIVE) {
        val toggleState = remember {
            mutableStateOf(false)
        }
        LaunchedEffect(toggleState) {
            toggleState.value = true


        }

        val p: Float by animateFloatAsState(
            if (toggleState.value) 1f else 0f,
            animationSpec = tween(5000),
            finishedListener = { onComplete.invoke() })

        proress = p

    }
    LinearProgressIndicator(modifier = Modifier, color = Color.Red, progress = proress)

}*/


package com.example.livechat.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonImage
import com.example.livechat.LCViewModel

enum class State {
    INITIAL, ACTIVE, COMPLETED
}

@Composable
fun SingleStatusScreen(navController: NavController, vm: LCViewModel, userId: String) {
    val statuses = vm.status.value.filter { it.user.userId == userId }

    if (statuses.isNotEmpty()) {
        val currentStatus = remember { mutableStateOf(0) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Display the current status image without zooming in
            CommonImage(
                data = statuses[currentStatus.value].imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit // Adjusted to fit without zooming
            )

            // Progress indicators at the bottom of the screen
            Row(modifier = Modifier.fillMaxWidth()) {
                statuses.forEachIndexed { index, status ->
                    CustomProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp) // Adjusted height for better visibility
                            .padding(horizontal = 2.dp),
                        state = when {
                            currentStatus.value < index -> State.INITIAL
                            currentStatus.value == index -> State.ACTIVE
                            else -> State.COMPLETED
                        }
                    ) {
                        if (currentStatus.value < statuses.size - 1) {
                            currentStatus.value++
                        } else {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomProgressIndicator(modifier: Modifier, state: State, onComplete: () -> Unit) {
    var progress = when (state) {
        State.INITIAL -> 0f
        State.COMPLETED -> 1f
        State.ACTIVE -> 0f // Will be animated to 1f
    }

    if (state == State.ACTIVE) {
        val toggleState = remember { mutableStateOf(false) }
        LaunchedEffect(toggleState) {
            toggleState.value = true
        }

        // Animate progress
        val animatedProgress: Float by animateFloatAsState(
            targetValue = if (toggleState.value) 1f else 0f,
            animationSpec = tween(durationMillis = 5000),
            finishedListener = { onComplete.invoke() }
        )

        progress = animatedProgress
    }

    LinearProgressIndicator(
        modifier = modifier,
        color = Color.Red,
        progress = progress
    )
}
