/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import com.aurora.gplayapi.data.providers.DeviceInfoProvider
import java.util.Locale

class AuthData internal constructor(val email: String) {

    var aasToken: String = String()
        internal set
    var authToken: String = String()
        internal set
    var isAnonymous: Boolean = false
        internal set
    var gsfId: String = String()
        internal set
    var tokenDispenserUrl: String = String()
        internal set
    var ac2dmToken: String = String()
        internal set
    var androidCheckInToken: String = String()
        internal set
    var deviceCheckInConsistencyToken: String = String()
        internal set
    var deviceConfigToken: String = String()
        internal set
    var experimentsConfigToken: String = String()
        internal set
    var gcmToken: String = String()
        internal set
    var oAuthLoginToken: String = String()
        internal set
    var dfeCookie: String = String()
        internal set
    var locale: Locale = Locale.getDefault()
        internal set
    var deviceInfoProvider: DeviceInfoProvider? = null
        internal set
    var userProfile: UserProfile? = null
        internal set
}
