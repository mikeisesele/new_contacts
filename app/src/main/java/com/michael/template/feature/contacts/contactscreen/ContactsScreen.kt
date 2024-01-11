package com.michael.template.feature.contacts.contactscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.ui.components.CenterColumn
import com.michael.template.core.ui.theme.Dimens.PaddingFourth
import com.michael.template.core.ui.theme.Dimens.PaddingHalf
import com.michael.template.feature.contacts.contactscreen.components.AnimatedDialog
import com.michael.template.feature.contacts.contactscreen.components.DialogConfig
import com.michael.template.feature.contacts.contactscreen.components.NestedListItemComponent
import com.michael.template.feature.contacts.contactscreen.components.NoResultScreen
import com.michael.template.feature.contacts.contactscreen.components.NoSearchResultScreen
import com.michael.template.feature.contacts.contactscreen.components.SearchBarComponent
import com.michael.template.feature.contacts.contactscreen.components.SyncingAnimation
import com.michael.template.feature.contacts.domain.model.NestedListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PROGRESS_STATE = 0.1f
private const val PROGRESS_STATE_DELAY = 500L
private const val COMPLETE_PROGRESS_STATE = 1.0f

@Composable
fun ContactScreen(
    contacts: ImmutableList<NestedListItem>,
    queriedContacts: ImmutableList<NestedListItem>,
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
                    Spacer(modifier = Modifier.padding(PaddingHalf))
                }

                if (emptyContent) {
                    EmptyContacts(persistingDays)
                } else if (queriedContacts.isEmpty() && searchQuery.isNotEmpty()) {
                    NoSearchResultScreen()
                } else {
                    finalContent.forEach { nestedListItem ->
                        NestedListItemComponent(nestedListItem)
                    }
                }
            }
            AnimatedDialog(dialogConfig = dialogConfig)
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
