package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.core.ui.theme.Dimens.PaddingDefault
import com.michael.template.util.Constants.guideLists

@Composable
fun InteractionGuide(dismissDialog: () -> Unit) {
    Dialog(
        onDismissRequest = { dismissDialog() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingHalf)
                .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(Dimens.RadiusDouble)),
        ) {
            item {
                Text(
                    color = Color.Black,
                    text = "Interaction Guide",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingDefault),
                )
            }
            items(guideLists) {
                val (action, result) = it
                Guides(action, result)
            }
        }
    }
}

@Composable
private fun Guides(
    action: String,
    result: String,
) {
    Row(
        modifier = Modifier
            .padding(PaddingDefault),
    ) {
        Text(text = action, color = Color.Black, style = TextStyle(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = result, color = Color.Black, style = TextStyle(fontWeight = FontWeight.Bold))
    }
}
