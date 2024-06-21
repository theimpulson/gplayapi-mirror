package com.aurora.gplayapi.utils

@Suppress("UNCHECKED_CAST")
internal fun <T : Any?> Any.dig(vararg keys: Any): T? {
    var current: Any? = this
    keys.forEach { key ->
        if (current == null) {
            return null
        }
        current = when (current) {
            is Collection<*> -> {
                val index = key.toString().toIntOrNull()
                index?.let { (current as Collection<*>).elementAtOrNull(it) }
            }

            is Map<*, *> -> {
                (current as Map<*, *>)[key]
            }

            else -> {
                null
            }
        }
    }
    return current as T
}
