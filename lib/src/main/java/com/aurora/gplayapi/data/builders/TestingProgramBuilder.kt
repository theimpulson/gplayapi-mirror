/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders

import com.aurora.gplayapi.AppDetails
import com.aurora.gplayapi.data.models.details.TestingProgram

internal object TestingProgramBuilder {

    fun build(appDetails: AppDetails): TestingProgram? {
        return if (appDetails.hasTestingProgramInfo()) {
            TestingProgram().apply {
                appDetails.testingProgramInfo?.let {
                    artwork = ArtworkBuilder.build(it.image)
                    displayName = it.displayName
                    email = it.email
                    isAvailable = true
                    isSubscribed = it.subscribed
                    isSubscribedAndInstalled = it.subscribedAndInstalled
                }
            }
        } else {
            null
        }
    }
}
