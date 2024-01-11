package com.michael.template.feature.contacts.domain.model

import com.michael.template.core.base.model.ImmutableList

data class NestedListItem(
    val timeFrame: String,
    val contacts: ImmutableList<ContactUiModel>,
)
