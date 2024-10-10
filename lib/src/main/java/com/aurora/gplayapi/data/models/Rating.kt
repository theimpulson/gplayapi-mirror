/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    var average: Float = 0f,
    var oneStar: Long = 0L,
    var twoStar: Long = 0L,
    var threeStar: Long = 0L,
    var fourStar: Long = 0L,
    var fiveStar: Long = 0L,
    var thumbsUp: Long = 0L,
    var thumbsDown: Long = 0L,
    var label: String = String(),
    var abbreviatedLabel: String = String()
) : Parcelable
