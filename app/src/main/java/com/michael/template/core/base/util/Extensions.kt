package com.michael.template.core.base.util

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.michael.template.util.Constants.FLOAT_FIVE
import com.michael.template.util.Constants.FLOAT_FOUR
import com.michael.template.util.Constants.THIRTY_SEVEN
import com.michael.template.util.Constants.THREE_SIXTY
import kotlin.math.absoluteValue

fun <T, K> Iterable<T>.toSetBy(selector: (T) -> K): Set<T> {
    val set = LinkedHashSet<T>()
    val keys = HashSet<K>()
    for (element in this) {
        val key = selector(element)
        if (keys.add(key)) {
            set.add(element)
        }
    }
    return set
}

fun String.normalize(): String {
    // Remove non-digit characters and leading country code
    return replace(Regex("[^\\d]"), "").removePrefix("+234").removePrefix("0")
}

@ColorInt
fun String.toHslColor(saturation: Float = FLOAT_FIVE, lightness: Float = FLOAT_FOUR): Int {
    val hue = fold(0) { acc, char -> char.code + acc * THIRTY_SEVEN } % THREE_SIXTY
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}
