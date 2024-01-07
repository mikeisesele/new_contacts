package com.michael.template.util

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun ZonedDateTime?.isWithinDaysFromToday(days: Long): Boolean = this?.let {
    val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
    it.isAfter(currentTime.minusDays(days)) && it.isBeforeOrEqual(currentTime)
} ?: false

fun ZonedDateTime.isBeforeOrEqual(other: ZonedDateTime): Boolean = isBefore(other) || isEqual(other)

@Suppress("MagicNumber")
fun ZonedDateTime.toReadable(): String {
    val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
    val hoursAgo = ChronoUnit.HOURS.between(this, currentTime)

    return when {
        hoursAgo in 0..23 -> "Added today"
        hoursAgo in 24..47 -> "Added yesterday"
        hoursAgo in 48..168 -> "Added earlier this week"
        hoursAgo in 169..335 -> "Added last week"
        hoursAgo in 336..480 -> "Added two weeks ago"
        hoursAgo in 481..623 -> "Added three weeks ago"
        hoursAgo >= 624 -> {
            val weeksAgo = hoursAgo / 168
            "Added $weeksAgo weeks ago"
        }
        else -> {
            // If the date is not recent, format it as "Added on" with a custom date format
            val formattedDate = this.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault()))
            "Added on $formattedDate"
        }
    }
}
