package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.common.dialPhoneNumber
import com.michael.template.core.common.shareContact
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

@Composable
fun ContactScreen(
    contacts: ImmutableList<ContactUiModel>,
    queriedContacts: ImmutableList<ContactUiModel>,
    onQueryChanged: (String) -> Unit,
    isLoading: Boolean,
    contactSynced: Boolean,
    searchQuery: String,
    dialogConfig: DialogConfig,
    persistingDays: String,
    deleteOldContacts: () -> Unit,
) {
    Box {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column {
                if (contacts.isEmpty() && contactSynced) {
                    EmptyContacts(persistingDays)
                } else if (queriedContacts.isEmpty() && searchQuery.isNotEmpty()) {
                    NoSearchResultScreen()
                } else {
                    val finalContent = queriedContacts.ifEmpty { contacts }
                    LazyColumn(
                        Modifier.padding(
                            top = Dimens.PaddingHalf,
                            start = Dimens.PaddingEighth,
                            end = Dimens.PaddingEighth,
                        ),
                    ) {
//                        item {
//                            Button(onClick = deleteOldContacts) {
//                                Text(text = "Delete Old Contacts")
//                            }
//                        }
                        if (finalContent.isNotEmpty()) {
                            item {
                                SearchBarComponent(onValueChange = onQueryChanged, searchQuery = searchQuery)
                            }
                        }
                        items(finalContent) {
                            ContactTile(contact = it)
                        }
                    }
                }
            }
            AnimatedDialog(dialogConfig = dialogConfig)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactTile(contact: ContactUiModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
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
                    shape = RoundedCornerShape(Dimens.RadiusDouble),
                )
                .combinedClickable(
                    onLongClick = { context.shareContact(contact) },
                    onClick = { context.dialPhoneNumber(contact.phones.first()) },
                ),
        ) {
            Text(
                modifier = Modifier.padding(Dimens.PaddingTwoThirds).weight(NAME_WEIGHT),
                text = contact.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = modifier.padding(Dimens.PaddingDefault))
            Column {
                Text(
                    modifier = Modifier.padding(
                        top = Dimens.PaddingTwoThirds,
                        start = Dimens.PaddingTwoThirds,
                        end = Dimens.PaddingTwoThirds,
                    ),
                    text = contact.phones.ifNotEmpty { first() } ?: "NAN",
                )
                Text(
                    modifier = Modifier
                        .padding(
                            Dimens.PaddingTwoThirds,
                        )
                        .align(Alignment.End),
                    text = contact.readableDateAdded,
                    fontSize = Dimens.FontSize10,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
private fun EmptyContacts(persistingDays: String) {
    NoResultScreen(persistingDays)
}

@Composable
private fun LoadingScreen() {
    CenterColumn {
        SyncingAnimation()
    }
}

private const val NAME_WEIGHT = 1.5f
