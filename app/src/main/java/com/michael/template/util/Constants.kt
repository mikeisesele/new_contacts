package com.michael.template.util

object Constants {
    const val IS_APP_ACTIVE = "is_app_active"
    const val PERSISTENCE_KEY = "persistence_days"
    const val DEFAULT_PERSISTENCE_SET = "default_persistence_set"
    const val ONE_MONTH = 30L
    const val TWO_MONTHS = 60L
    const val THREE_MONTH = 90L
    const val WORKER_NAME = "contact_sync_worker"
    const val FIFTEEN = 15L
    val guideLists = listOf(
        "To call contact" to "Single click",
        "To share contact" to "Double click",
        "Chat on Whatsapp" to "Long press",
    )
}
