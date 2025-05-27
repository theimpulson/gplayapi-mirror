/*
 * SPDX-FileCopyrightText: 2023-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.utils

import kotlin.reflect.KClass

private val defaults = mapOf<KClass<*>, Any?>(
    Boolean::class to false,
    String::class to "",
    Int::class to 0,
    Long::class to 0L,
    Double::class to 0.0,
    List::class to emptyList<Any>(),
)

internal inline fun <reified T> default(): T {
    return defaults[T::class] as T
        ?: throw IllegalArgumentException("No default value defined for type ${T::class}")
}

internal inline fun <reified T> Any?.dig(vararg keys: Any): T {
    var current: Any? = this

    for (key in keys) {
        current = when (current) {
            is Map<*, *> -> current[key]
            is List<*> -> (key as? Int)?.let { current.getOrNull(it) }
            else -> return default()
        }
    }

    return (current as? T) ?: default()
}

internal inline fun <reified T> Any?.digOrDefault(default: T, vararg keys: Any): T {
    var current: Any? = this

    for (key in keys) {
        current = when (current) {
            is Map<*, *> -> current[key]
            is List<*> -> (key as? Int)?.let { current.getOrNull(it) }
            else -> return default
        }
    }

    return (current as? T) ?: default
}
