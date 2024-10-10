/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.exceptions

internal sealed class InternalException : Exception() {
    data class AuthException(val reason: String = "Authentication Error") : InternalException()
    data class AppNotPurchased(val reason: String = "App not purchased") : InternalException()

    data class AppNotFound(val reason: String = "App not found, maybe restricted (OEM or Geo)") :
        InternalException()

    data class AppRemoved(val reason: String = "App removed from Play Store") : InternalException()

    data class AppNotSupported(val reason: String = "App not supported") : InternalException()

    data class EmptyDownloads(val reason: String = "File list empty") :
        InternalException() // Not sure about the root cause.

    data class Unknown(val reason: String = "¯\\_(ツ)_/¯") : InternalException()

    data class Server(val code: Int = 500, val reason: String = "Server error") : InternalException()
}
