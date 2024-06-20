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

package com.aurora.gplayapi.data.models

import com.aurora.gplayapi.data.providers.DeviceInfoProvider
import java.util.Locale

class AuthData {

    internal constructor(email: String, aasToken: String) {
        this.email = email
        this.aasToken = aasToken
    }

    internal constructor(email: String, authToken: String, isAnonymous: Boolean) {
        this.email = email
        this.authToken = authToken
        this.isAnonymous = isAnonymous
    }

    var email: String = String()
        internal set
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
