/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.details

import android.os.Parcelable
import com.aurora.gplayapi.data.models.Artwork
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestingProgram(
    var artwork: Artwork = Artwork(),
    var displayName: String = String(),
    var email: String = String(),
    var isAvailable: Boolean = false,
    var isSubscribed: Boolean = false,
    var isSubscribedAndInstalled: Boolean = false
) : Parcelable

@Parcelize
data class TestingProgramStatus(
    var subscribed: Boolean = false,
    var unsubscribed: Boolean = false
) : Parcelable
