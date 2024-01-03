package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

private const val INITIAL = 1f
private const val TARGET = 0f
private const val DURATION = 1000

@Composable
fun SyncingAnimation() {
    val alpha = remember { mutableFloatStateOf(INITIAL) }
    LaunchedEffect(Unit) {
        animate(
            initialValue = INITIAL,
            targetValue = TARGET,
            animationSpec = infiniteRepeatable(
                animation = tween(DURATION),
                repeatMode = RepeatMode.Reverse,
            ),
        ) { value, _ ->
            alpha.floatValue = value
        }
    }
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "Syncing Contacts",
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    alpha = alpha.floatValue,
                ),
        )
    }
}
