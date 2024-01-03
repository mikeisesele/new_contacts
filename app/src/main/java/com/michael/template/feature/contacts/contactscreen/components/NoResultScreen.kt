package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.extensions.primaryTextStyle
import com.michael.template.core.ui.extensions.secondaryTextStyle
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.core.ui.theme.Dimens.FontSizeDefault
import com.michael.template.core.ui.theme.Dimens.Text30

@Composable
fun NoResultScreen(persistingDays: String) {
    val context = LocalContext.current

//    Scaffold {

//        Box(contentAlignment = Alignment.BottomStart) {
//            AsyncImage(
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop,
//                model = "file:///assets/$error_ic_noResult",
//                contentDescription = null,
//            )
//            Image(
//                modifier = Modifier.fillMaxSize(),
//                bitmap = painterResource(id = R.drawable.error_ic_noresult),
//                contentScale = ContentScale.Crop,
//                contentDescription = null,
//            )

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
            text = "Empty",
            style = primaryTextStyle(fontSize = Text30, color = White),
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingDoubleThreeFourths))
        Text(
            text = "Sorry, there are no saved contacts in the last $persistingDays days.",
            style = secondaryTextStyle(color = White, fontSize = FontSizeDefault, textAlign = TextAlign.Center),
            lineHeight = Dimens.LineHeightDefault,
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingDoubleThreeFourths))
    }
}
