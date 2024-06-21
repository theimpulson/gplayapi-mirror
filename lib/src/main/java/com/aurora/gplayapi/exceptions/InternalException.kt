/*
 *     GPlayApi
 *     Copyright (C) 2020  Aurora OSS
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
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
