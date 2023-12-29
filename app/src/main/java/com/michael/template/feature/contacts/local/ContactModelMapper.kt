package com.michael.template.feature.contacts.local

import com.michael.template.domain.ContactModel
import com.michael.template.domain.OldContactModel
import java.time.LocalDateTime

fun OldContactModel.toContactModel(): ContactModel = ContactModel(
    id = id,
    name = name,
    phones = phones,
    dateAdded = LocalDateTime.now(),
)
