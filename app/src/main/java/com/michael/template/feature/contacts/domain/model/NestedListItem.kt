package com.michael.template.feature.contacts.domain.model

import com.michael.template.core.base.model.ImmutableList

data class NestedListItem(
    val timeFrame: String,
    val contacts: ImmutableList<ContactUiModel>,
)

interface NestedListContentType {
    data class Header(val header: String) : NestedListContentType
    interface Content : NestedListContentType
}
