package com.michael.template.feature.contacts.domain.model

import java.time.ZonedDateTime

data class ContactUiModel(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: ZonedDateTime,
    val readableDateAdded: String,
) : NestedListContentType.Content
