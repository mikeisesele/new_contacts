package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michael.template.core.ui.theme.Dimens

@Composable
fun FloatingActionButtonComponent(
    onClicked: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClicked,
        modifier = Modifier.padding(Dimens.PaddingDefault),
    ) {
        Icon(Icons.Filled.List, null)
    }
}
