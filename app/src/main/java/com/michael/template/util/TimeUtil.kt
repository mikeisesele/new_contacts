package com.michael.template.util

import com.michael.template.util.Constants.ONE
import com.michael.template.util.Constants.ONE_THOUSAND
import com.michael.template.util.Constants.SIXTY
import com.michael.template.util.Constants.THIRTY
import com.michael.template.util.Constants.THREE_SIX_FIVE
import com.michael.template.util.Constants.TWELVE
import com.michael.template.util.Constants.TWENTY_FOUR
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime?.isWithinDaysFromToday(days: Long): Boolean = this?.let {
    val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
    it.isAfter(currentTime.minusDays(days)) && it.isBeforeOrEqual(currentTime)
} ?: false

fun ZonedDateTime.isBeforeOrEqual(other: ZonedDateTime): Boolean = isBefore(other) || isEqual(other)

@Suppress("ComplexMethod")
fun ZonedDateTime.toReadable(): String {
    val diff = ZonedDateTime.now().toInstant().toEpochMilli() - toInstant().toEpochMilli()

    val oneSec = ONE_THOUSAND
    val oneMin: Long = SIXTY * oneSec
    val oneHour: Long = SIXTY * oneMin
    val oneDay: Long = TWENTY_FOUR * oneHour
    val oneMonth: Long = THIRTY * oneDay
    val oneYear: Long = THREE_SIX_FIVE * oneDay

    val diffMin: Long = diff / oneMin
    val diffHours: Long = diff / oneHour
    val diffDays: Long = diff / oneDay
    val diffMonths: Long = diff / oneMonth
    val diffYears: Long = diff / oneYear

    return when {
        diffYears > 0 -> {
            "$diffYears years ago"
        }
        diffMonths > 0 && diffYears < ONE -> {
            val months = (diffMonths - diffYears / TWELVE)
            val monthsDisplay = if (months == ONE) "month" else "months"
            "$monthsDisplay months ago"
        }
        diffDays > 0 && diffMonths < ONE -> {
            val day = (diffDays - diffMonths / THIRTY)
            val daysDisplay = if (day > ONE) "days" else "day"
            "$day $daysDisplay ago"
        }
        diffHours > 0 && diffDays < ONE -> {
            val hours = (diffHours - diffDays * TWENTY_FOUR)
            val hoursDisplay = if (hours > ONE) "hours" else "hour"
            "$hours $hoursDisplay ago"
        }
        diffMin > 0 && diffHours < ONE -> {
            val min = diffMin % SIXTY
            val minDisplay = if (min == ONE) "minute" else "minutes"
            return "$min $minDisplay ago"
        }
        diffMin < ONE -> {
            "just now"
        }

        else -> {
            "Added some time ago"
        }
    }
}
