package com.livadoo.shared.extension

import kotlin.random.Random

private const val ALPHABET_WITH_NUMBERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
private val random = Random.Default

fun generateId(prefix: String = "", length: Int): String {
    val builder = StringBuilder()

    repeat(length) {
        val randomIndex = random.nextInt(ALPHABET_WITH_NUMBERS.length)
        builder.append(ALPHABET_WITH_NUMBERS[randomIndex])
    }

    return "$prefix$builder"
}
