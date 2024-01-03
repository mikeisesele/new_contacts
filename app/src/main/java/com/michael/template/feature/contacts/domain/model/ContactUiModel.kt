package com.michael.template.feature.contacts.domain.model

import java.time.LocalDateTime
data class ContactUiModel(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: LocalDateTime,
    val readableDateAdded: String,
)
