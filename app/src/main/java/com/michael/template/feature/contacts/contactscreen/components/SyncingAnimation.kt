package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.components.Spacer
import com.michael.template.core.ui.theme.Dimens

private const val INITIAL = 1f
private const val TARGET = 0f
private const val DURATION = 1000

@Composable
fun SyncingAnimation(progress: Float) {
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

    CenterColumn(Modifier.fillMaxSize()) {
        LinearProgressIndicator(
            progress = progress,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .height(Dimens.height)
                .clip(RoundedCornerShape(Dimens.RadiusQuadruple)),
            trackColor = MaterialTheme.colorScheme.secondary,
        )
        Spacer(vertical = Dimens.PaddingDoubleThreeFourths)
        Text(
            text = "Syncing Contacts",
            modifier = Modifier
                .graphicsLayer(
                    alpha = alpha.floatValue,
                ),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            ),
        )
    }
}
