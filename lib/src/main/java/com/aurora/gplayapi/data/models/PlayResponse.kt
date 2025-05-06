/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import java.util.UUID

data class PlayResponse(
    val id: String = UUID.randomUUID().toString(),
    val responseBytes: ByteArray = byteArrayOf(),
    val errorBytes: ByteArray = byteArrayOf(),
    val errorString: String = ("No Error"),
    val isSuccessful: Boolean = false,
    val code: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is PlayResponse -> other.id == id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
