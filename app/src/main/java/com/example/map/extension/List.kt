package com.example.map.extension

import androidx.core.util.Predicate

public fun <T> List<T>.find(predicate: Predicate<T>): T? {
    return firstOrNull { predicate.test(it) }
}