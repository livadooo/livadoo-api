package com.livadoo.shared.extension

fun String.containsExceptionKey(key: String): Boolean {
    return contains("dup key: { $key")
}
