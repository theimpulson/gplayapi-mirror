package com.aurora.gplayapi.utils

import java.util.concurrent.atomic.AtomicInteger

object Commons {
    private val counter = AtomicInteger()

    fun getUniqueId(): Int {
        return counter.incrementAndGet()
    }
}