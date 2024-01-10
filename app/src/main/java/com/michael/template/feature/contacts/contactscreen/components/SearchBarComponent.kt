package com.michael.template.feature.contacts.contactscreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.michael.template.core.ui.extensions.primaryTextStyle
import com.michael.template.core.ui.theme.Dimens

private const val UNFOCUSED_ALPHA = 0.4f

@Composable
fun SearchBarComponent(
    onValueChange: (String) -> Unit,
    searchQuery: String,
) {
    var queryValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = searchQuery,
                selection = TextRange(searchQuery.length),
            ),
        )
    }

    OutlinedTextField(
        textStyle = primaryTextStyle(),
        value = queryValue,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = Modifier.padding(Dimens.PaddingDefault).fillMaxWidth(),
        placeholder = { Text(text = "Search") },
        shape = RoundedCornerShape(Dimens.RadiusDouble),
        onValueChange = {
            queryValue = it
            onValueChange(it.text)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        keyboardActions = KeyboardActions(),
    )
}
