package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.extensions.clickable
import com.michael.template.core.ui.theme.Dimens
import com.michael.template.core.ui.theme.Dimens.Padding40
import com.michael.template.feature.contacts.domain.model.MONTHS

@Composable
fun DefaultOptionsComponent(onDurationSelected: (MONTHS) -> Unit) {
    CenterColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(Dimens.PaddingDefaultOneFourths),
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = Dimens.PaddingDouble,
                horizontal = Padding40,
            ),
            text = "How long should we display your newly saved contacts",
            textAlign = TextAlign.Center,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            MONTHS.values().forEach {
                val months = if (it.ordinal == 0) "Month" else "Months"
                ElevatedCard(
                    modifier = Modifier.clickable(onClick = { onDurationSelected(it) }),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(Dimens.PaddingDefault),
                    ) {
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = it.ordinal.plus(1).toString(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.padding(bottom = Dimens.PaddingFourth))
                        Text(
                            text = months,
                            modifier = Modifier.wrapContentWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDefaultOptionsComponent() {
    DefaultOptionsComponent(onDurationSelected = {})
}
