/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.DeviceManager
import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.providers.DeviceInfoProvider
import com.aurora.gplayapi.network.DefaultHttpClient
import com.aurora.gplayapi.network.IHttpClient
import java.util.Locale
import java.util.Properties

object AuthHelper {

    enum class Token {
        AAS, AUTH
    }

    private var httpClient: IHttpClient = DefaultHttpClient

    fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    /**
     * Builds and returns authentication data to work with the helper methods
     * @param email E-mail address for the account
     * @param token Token for account authentication
     * @param tokenType Type of the token, can be either [Token.AAS] or [Token.AUTH]
     * @param isAnonymous If the account credentials are anonymous (e.g. provided by Aurora Dispenser), defaults to false
     * @param properties Properties of the device, defaults to Pixel 7a properties
     * @param locale Locale for the account, default otherwise
     * @return An instance of [AuthData]
     */
    fun build(
        email: String,
        token: String,
        tokenType: Token,
        isAnonymous: Boolean = false,
        properties: Properties = DeviceManager.loadProperties("px_7a.properties")!!,
        locale: Locale = Locale.getDefault()
    ): AuthData {
        val deviceInfoProvider = DeviceInfoProvider(properties, locale.toString())
        val authData = AuthData(email)

        // Token
        if (tokenType == Token.AAS) {
            authData.aasToken = token
        } else {
            authData.authToken = token
        }

        authData.deviceInfoProvider = deviceInfoProvider
        authData.locale = locale
        authData.isAnonymous = isAnonymous

        val api = GooglePlayApi(authData).via(httpClient)

        // Android GSF ID
        val gsfId = api.generateGsfId(deviceInfoProvider)
        authData.gsfId = gsfId

        // Upload Device Config
        val deviceConfigResponse = api.uploadDeviceConfig(deviceInfoProvider)
        authData.deviceConfigToken = deviceConfigResponse.uploadDeviceConfigToken

        // Generate AuthToken if we are working with AAS
        if (tokenType == Token.AAS) {
            val authToken = api.generateToken(token, GooglePlayApi.Service.GOOGLE_PLAY)
            authData.authToken = authToken
        }

        // Accept GooglePlay TOS
        api.toc()

        // Fetch UserProfile
        authData.userProfile = UserProfileHelper(authData).getUserProfile()

        return authData
    }

    /**
     * Validates given AuthData by fetching a test application
     * @param authData [AuthData] to validate
     */
    fun isValid(authData: AuthData): Boolean {
        return try {
            val testPackageName = "com.android.chrome"
            val app = AppDetailsHelper(authData)
                .using(httpClient)
                .getAppByPackageName(testPackageName)

            app.packageName == testPackageName && app.versionCode != 0
        } catch (e: Exception) {
            false
        }
    }
}
