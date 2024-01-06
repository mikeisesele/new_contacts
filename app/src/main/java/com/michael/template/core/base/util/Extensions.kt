package com.michael.template.core.base.util

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
