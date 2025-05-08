/*
 * SPDX-FileCopyrightText: 2024 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.utils

import java.util.concurrent.atomic.AtomicInteger

internal object Commons {
    private val counter = AtomicInteger()

    fun getUniqueId(): Int {
        return counter.incrementAndGet()
    }
}
