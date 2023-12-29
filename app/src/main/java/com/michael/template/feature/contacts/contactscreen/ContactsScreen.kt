package com.michael.template.feature.contacts.contactscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.ui.theme.Dimens
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

@Composable
fun ContactScreen(
    contacts: ImmutableList<ContactUiModel>,
    isLoading: Boolean,
    clearDB: () -> Unit,
) {
    if (isLoading) {
        LoadingScreen()
    } else {
        LazyColumn(
            Modifier.padding(top = Dimens.PaddingHalf, start = Dimens.PaddingEighth, end = Dimens.PaddingEighth),
        ) {
//            item {
//                Button(onClick = clearDB) {
//                    Text(text = "Clear DB")
//                }
//            }
            if (contacts.isEmpty()) {
                item {
                    EmptyContacts()
                }
            } else {
                items(contacts) {
                    ContactTile(contact = it)
                }
            }
        }
    }
}

@Composable
private fun ContactTile(contact: ContactUiModel, modifier: Modifier = Modifier) {
    Column(
        Modifier.padding(top = Dimens.PaddingHalf, start = Dimens.PaddingHalf, end = Dimens.PaddingHalf),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = Dimens.PaddingHalf, start = Dimens.PaddingHalf, end = Dimens.PaddingHalf)
                .border(
                    border = BorderStroke(Dimens.BorderStrokeWidth, color = Color.White),
                    shape = RoundedCornerShape(
                        Dimens.RadiusDouble,
                    ),
                ),
        ) {
            Text(modifier = Modifier.padding(Dimens.PaddingTwoThirds), text = contact.name)
            Spacer(modifier = modifier.weight(1f))
            Text(
                modifier = Modifier.padding(Dimens.PaddingTwoThirds),
                text = contact.phones.ifNotEmpty { first() } ?: "NAN",
            )
        }
        Spacer(modifier = modifier.height(Dimens.PaddingEighth))
        Text(
            modifier = Modifier
                .padding(
                    top = Dimens.PaddingTwoThirds,
                    end = Dimens.PaddingTwoThirds,
                )
                .align(Alignment.End),
            text = contact.readableDateAdded,
            fontSize = Dimens.FontSizeDefault,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun EmptyContacts() {
    Column {
        Text(text = "No New Contacts added in last 30 days", overflow = TextOverflow.Clip)
    }
}

@Composable
private fun LoadingScreen() {
    Column {
        Text(text = "Syncing Contacts")
    }
}
