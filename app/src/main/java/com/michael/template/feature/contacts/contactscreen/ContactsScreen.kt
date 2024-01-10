package com.michael.template.feature.contacts.contactscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.util.toHslColor
import com.michael.template.core.common.dialPhoneNumber
import com.michael.template.core.common.openWhatsApp
import com.michael.template.core.common.shareContact
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.core.ui.theme.Dimens.PaddingEighth
import com.michael.template.core.ui.theme.Dimens.PaddingFourth
import com.michael.template.core.ui.theme.Dimens.PaddingTwoThirds
import com.michael.template.feature.contacts.contactscreen.components.AnimatedDialog
import com.michael.template.feature.contacts.contactscreen.components.DialogConfig
import com.michael.template.feature.contacts.contactscreen.components.NoResultScreen
import com.michael.template.feature.contacts.contactscreen.components.NoSearchResultScreen
import com.michael.template.feature.contacts.contactscreen.components.SearchBarComponent
import com.michael.template.feature.contacts.contactscreen.components.SyncingAnimation
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import com.michael.template.util.Constants.FLOAT_FIVE
import com.michael.template.util.Constants.FLOAT_NINE
import com.michael.template.util.Constants.FLOAT_SEVEN
import com.michael.template.util.Constants.FLOAT_TWO
import com.michael.template.util.Constants.NAME_WEIGHT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

private const val PROGRESS_STATE = 0.1f
private const val PROGRESS_STATE_DELAY = 500L
private const val COMPLETE_PROGRESS_STATE = 1.0f

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
) {
    var progressState by remember { mutableFloatStateOf(PROGRESS_STATE) }

    val progress by animateFloatAsState(
        targetValue = progressState,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "",
    )

    LaunchedEffect(key1 = progress) {
        launch {
            while (progressState < COMPLETE_PROGRESS_STATE) {
                delay(PROGRESS_STATE_DELAY)
                progressState += PROGRESS_STATE
            }
            if (!isLoading) {
                progressState = COMPLETE_PROGRESS_STATE
            }
        }
    }

    val finalContent = queriedContacts.ifEmpty { contacts }
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingScreen(progress)
        } else {
            val emptyContent = contacts.isEmpty() && contactSynced

            Column {
                if (!emptyContent) {
                    Spacer(modifier = Modifier.padding(PaddingFourth))
                    SearchBarComponent(onValueChange = onQueryChanged, searchQuery = searchQuery)
                    Spacer(modifier = Modifier.padding(PaddingTwoThirds))
                }

                if (emptyContent) {
                    EmptyContacts(persistingDays)
                } else if (queriedContacts.isEmpty() && searchQuery.isNotEmpty()) {
                    NoSearchResultScreen()
                } else {
                    LazyColumn {
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

@Suppress("LongMethod")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactTile(contact: ContactUiModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        Modifier.padding(start = Dimens.PaddingHalf, end = Dimens.PaddingHalf),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    top = PaddingFourth,
                    start = Dimens.PaddingHalf,
                    end = Dimens.PaddingHalf,
                    bottom = PaddingEighth,
                )
                .border(
                    border = BorderStroke(
                        width = Dimens.BorderStrokeWidth,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = FLOAT_TWO),
                    ),
                    shape = RoundedCornerShape(Dimens.RadiusDouble),
                )
                .combinedClickable(
                    onClick = { context.dialPhoneNumber(contact.phones.first()) },
                    onDoubleClick = { context.shareContact(contact) },
                    onLongClick = { context.openWhatsApp(contact.phones.first()) },
                ),
        ) {
            Box(
                modifier = Modifier.padding(
                    start = Dimens.PaddingDefault,
                    top = Dimens.PaddingDefault,
                    bottom = Dimens.PaddingDefault,
                ).size(Dimens.Size36),
                contentAlignment = Alignment.Center,
            ) {
                val color = remember(contact.id, contact.name, contact.phones.first().hashCode()) {
                    Color("${contact.id} / ${contact.name}".toHslColor())
                }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(SolidColor(color))
                }
                Text(
                    text = contact.name.first().uppercase(),
                    color = Color.White.copy(alpha = FLOAT_SEVEN),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.FontSizeDefault,
                    ),
                )
            }
            Text(
                modifier = Modifier.padding(PaddingTwoThirds).weight(NAME_WEIGHT),
                text = contact.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = FLOAT_NINE),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = Dimens.FontSizeDefault),
            )
            Spacer(modifier = modifier.padding(Dimens.PaddingDefault))
            Column {
                Text(
                    modifier = Modifier.padding(
                        start = PaddingTwoThirds,
                        end = PaddingTwoThirds,
                        top = PaddingFourth,

                    ),
                    text = contact.phones.ifNotEmpty { first() } ?: "NAN",
                    color = MaterialTheme.colorScheme.primary.copy(alpha = FLOAT_SEVEN),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = Dimens.FontSize14),
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = PaddingTwoThirds)
                        .align(Alignment.End),
                    text = contact.readableDateAdded,
                    fontSize = Dimens.FontSize10,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = FLOAT_FIVE),
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
private fun LoadingScreen(progress: Float) {
    CenterColumn {
        SyncingAnimation(progress)
    }
}
