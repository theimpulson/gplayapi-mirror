/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.exceptions

import java.io.IOException

class GooglePlayException(message: String?) : IOException(message) {
    var code = 0
    var rawResponse: String = String()
}
