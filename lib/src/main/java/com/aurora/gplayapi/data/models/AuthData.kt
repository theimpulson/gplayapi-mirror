/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import com.aurora.gplayapi.data.providers.DeviceInfoProvider
import java.util.Locale

class AuthData {

    private constructor()

    constructor(email: String, aasToken: String) {
        this.email = email
        this.aasToken = aasToken
    }

    constructor(email: String, authToken: String, insecure: Boolean = true) {
        this.email = email
        this.authToken = authToken
        this.isAnonymous = true
    }

    var email: String = String()
    var aasToken: String = String()
    var isAnonymous: Boolean = false
    var authToken: String = String()
    var gsfId: String = String()
    var tokenDispenserUrl: String = String()
    var ac2dmToken: String = String()
    var androidCheckInToken: String = String()
    var deviceCheckInConsistencyToken: String = String()
    var deviceConfigToken: String = String()
    var experimentsConfigToken: String = String()
    var gcmToken: String = String()
    var oAuthLoginToken: String = String()
    var dfeCookie: String = String()
    var locale: Locale = Locale.getDefault()
    var deviceInfoProvider: DeviceInfoProvider? = null
    var userProfile: UserProfile? = null
}
