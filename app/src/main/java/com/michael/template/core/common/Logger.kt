package com.michael.template.core.common

import android.util.Log

fun <T> Any.log(clazz: Class<T>, line: Any) {
    clazz.let {
        Log.d("SERGIO-$line", "class: ${it.simpleName}, data: $this")
    }
}
