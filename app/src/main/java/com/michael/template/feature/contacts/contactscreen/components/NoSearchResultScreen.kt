package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.extensions.primaryTextStyle
import com.michael.template.core.ui.extensions.secondaryTextStyle
import com.michael.template.core.ui.theme.Dimens

@Composable
fun NoSearchResultScreen() {
    CenterColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                start = Dimens.Padding35,
                end = Dimens.Padding40,
                bottom = Dimens.Padding55,
            ),
    ) {
        Text(
            text = "No Results",
            style = primaryTextStyle(fontSize = Dimens.Text30, color = Color.White),
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingDoubleThreeFourths))
        Text(
            text = "Sorry, there are no contacts matching your query",
            style = secondaryTextStyle(
                color = Color.White,
                fontSize = Dimens.FontSizeDefault,
                textAlign = TextAlign.Center,
            ),
            lineHeight = Dimens.LineHeightDefault,
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingDoubleThreeFourths))
    }
}
