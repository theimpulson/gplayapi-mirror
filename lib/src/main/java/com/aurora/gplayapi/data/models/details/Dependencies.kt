/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.details

import android.os.Parcelable
import com.aurora.gplayapi.data.models.App
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dependencies(
    var dependentPackages: MutableList<String> = mutableListOf(),
    var dependentSplits: MutableList<String> = mutableListOf(),
    var dependentLibraries: MutableList<App> = mutableListOf(),
    var targetSDK: Int = -1,
    var totalSize: Long = -1L
) : Parcelable
