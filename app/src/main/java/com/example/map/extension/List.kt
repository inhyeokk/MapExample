package com.example.map.extension

import androidx.core.util.Predicate

fun <T> List<T>.find(predicate: Predicate<T>): T? {
    return firstOrNull { predicate.test(it) }
}
