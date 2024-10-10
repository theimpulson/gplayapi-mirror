/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.network.IHttpClient

class AuthValidator(authData: AuthData) : NativeHelper(authData) {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    fun isValid(): Boolean {
        return try {
            val testPackageName = "com.android.chrome"
            val app = AppDetailsHelper(authData)
                .using(httpClient)
                .getAppByPackageName(testPackageName)

            app.packageName == testPackageName
        } catch (e: Exception) {
            false
        }
    }
}
