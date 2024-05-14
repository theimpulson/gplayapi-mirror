/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

class PlayResponse {
    var responseBytes: ByteArray = byteArrayOf()
    var errorBytes: ByteArray = byteArrayOf()
    var errorString: String = ("No Error")
    var isSuccessful: Boolean = false
    var code: Int = 0
}
