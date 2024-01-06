package com.michael.template.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun LocalDateTime?.isWithinDaysFromToday(days: Long): Boolean = this?.let {
    val currentTime = LocalDateTime.now()
    it.isAfter(currentTime.minusDays(days)) && it.isBeforeOrEqual(currentTime)
} ?: false

fun LocalDateTime.isBeforeOrEqual(other: LocalDateTime): Boolean = isBefore(other) || isEqual(other)

@Suppress("MagicNumber")
fun LocalDateTime.toReadable(): String {
    val currentTime = LocalDateTime.now().toLocalDate().atStartOfDay()
    val inputTime = this.toLocalDate().atStartOfDay()
    val daysAgo = ChronoUnit.DAYS.between(inputTime, currentTime)

    return when {
        daysAgo == 0L -> "Added today"
        daysAgo == 1L -> "Added yesterday"
        daysAgo in 2..6 -> "Added earlier this week"
        daysAgo in 7..13 -> "Added last week"
        daysAgo in 14..20 -> "Added two weeks ago"
        daysAgo in 21..27 -> "Added three weeks ago"
        daysAgo >= 28 -> {
            val weeksAgo = daysAgo / 7
            "Added $weeksAgo weeks ago"
        }
        else -> {
            // You can customize this for cases when it's negative or other scenarios
            val formattedDate = this.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault()))
            "Added on $formattedDate"
        }
    }
}
