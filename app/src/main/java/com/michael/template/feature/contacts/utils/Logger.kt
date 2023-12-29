package com.michael.template.feature.contacts.utils

import android.util.Log

fun <T> Any.log(clazz: Class<T>, line: Any) {
    clazz.let {
        Log.d("INFORMATION-$line", "class: ${it.simpleName}, data: $this")
    }
}
