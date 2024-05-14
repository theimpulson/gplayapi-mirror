/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.exceptions

sealed class ApiException {

    data class AppNotPurchased(val reason: String = "App not purchased") : Exception()

    data class AppNotFound(val reason: String = "App not found, maybe restricted (OEM or Geo)") :
        Exception()

    data class AppRemoved(val reason: String = "App removed from Play Store") : Exception()

    data class AppNotSupported(val reason: String = "App not supported") : Exception()

    data class EmptyDownloads(val reason: String = "File list empty") :
        Exception() // Not sure about the root cause.

    data class Unknown(val reason: String = "¯\\_(ツ)_/¯") : Exception()

    data class Server(val code: Int = 500, val reason: String = "Server error") : Exception()
}
