package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.michael.template.R
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.components.Spacer
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.core.ui.theme.Dimens.IconSizeExtraLarge

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
    CenterColumn(Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .size(IconSizeExtraLarge)
                .graphicsLayer(
                    alpha = alpha.floatValue,
                ),
            painter = painterResource(id = R.drawable.sync_contact_icon_white),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
        Spacer(vertical = Dimens.PaddingDoubleThreeFourths)
        Text(
            text = "Syncing Contacts",
            modifier = Modifier
                .graphicsLayer(
                    alpha = alpha.floatValue,
                ),
        )
    }
}
