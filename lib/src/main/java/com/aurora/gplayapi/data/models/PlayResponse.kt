/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

data class PlayResponse(
    val responseBytes: ByteArray = byteArrayOf(),
    val errorBytes: ByteArray = byteArrayOf(),
    val errorString: String = ("No Error"),
    val isSuccessful: Boolean = false,
    val code: Int = 0
)
