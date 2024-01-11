package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import com.michael.template.feature.contacts.domain.model.NestedListContentType
import com.michael.template.util.Constants
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

@Composable
fun NestedListItemComponent(
    nestedListItem: ImmutableList<NestedListContentType>,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.PaddingHalf),
    ) {
        itemsIndexed(nestedListItem) { index, nestedListContent ->
//            val isNextItemHeader = index == nestedListItem.lastIndex ||
//                nestedListItem[index + 1] is NestedListContentType.Header
//            val isPreviousItemHeader = index > 0 &&
//                    nestedListItem[index - 1] is NestedListContentType.Header

            when (nestedListContent) {
                is NestedListContentType.Header -> {
                    ContactTileHeader(nestedListContent.header)
                }
                is ContactUiModel -> {
                    ContactTile(contact = nestedListContent)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(Dimens.PaddingDefault))
        }
    }
}

@Composable
private fun ContactTileHeader(header: String) {
    Text(
        text = header,
        modifier = Modifier.padding(
            Dimens.PaddingDefault,
        ).fillMaxWidth(),
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
        ),
    )
}

@Suppress("LongMethod")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactTile(contact: ContactUiModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                top = Dimens.PaddingFourth,
                start = Dimens.PaddingHalf,
                end = Dimens.PaddingHalf,
                bottom = Dimens.PaddingEighth,
            )
            .border(
                border = BorderStroke(
                    width = Dimens.BorderStrokeWidth,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = Constants.FLOAT_TWO),
                ),
                shape = RoundedCornerShape(Dimens.RadiusDouble),
            )
            .combinedClickable(
                onClick = { context.dialPhoneNumber(contact.phones.first()) },
                onDoubleClick = { context.shareContact(contact) },
                onLongClick = { context.openWhatsApp(contact.phones.first()) },
            ),
    ) {
        ContactNameComponent(contact)
        Spacer(modifier = modifier.padding(Dimens.PaddingDefault))
        ContactDetailsComponent(contact)
    }
}

@Composable
private fun RowScope.ContactNameComponent(contact: ContactUiModel) {
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
            color = Color.White.copy(alpha = Constants.FLOAT_SEVEN),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = Dimens.FontSizeDefault,
            ),
        )
    }
    Text(
        modifier = Modifier.padding(Dimens.PaddingTwoThirds).weight(Constants.NAME_WEIGHT),
        text = contact.name,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        color = MaterialTheme.colorScheme.secondary.copy(alpha = Constants.FLOAT_NINE),
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = Dimens.FontSizeDefault),
    )
}

@Composable
private fun ContactDetailsComponent(contact: ContactUiModel) {
    Column {
        Text(
            modifier = Modifier.padding(
                start = Dimens.PaddingTwoThirds,
                end = Dimens.PaddingTwoThirds,
                top = Dimens.PaddingFourth,

            ),
            text = contact.phones.ifNotEmpty { first() } ?: "NAN",
            color = MaterialTheme.colorScheme.primary.copy(alpha = Constants.FLOAT_SEVEN),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = Dimens.FontSize14),
        )
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.PaddingTwoThirds)
                .align(Alignment.End),
            text = contact.readableDateAdded,
            fontSize = Dimens.FontSize10,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = Constants.FLOAT_FIVE),
        )
    }
}
