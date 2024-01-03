package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedDialog(
    dialogConfig: DialogConfig,
) {
    AnimatedVisibility(
        visible = dialogConfig.visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            content = dialogConfig.dialogContent,
        )
    }
}

data class DialogConfig(
    /**
     * Parent of this content is a Box that takes full width and height
     */
    val dialogContent: @Composable (BoxScope.() -> Unit) = {},
    val visible: Boolean = false,
)
