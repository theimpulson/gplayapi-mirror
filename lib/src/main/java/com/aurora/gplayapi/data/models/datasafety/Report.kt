/*
 * SPDX-FileCopyrightText: 2024 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.datasafety

import android.os.Parcelable
import com.aurora.gplayapi.data.models.Artwork
import com.aurora.gplayapi.data.models.DeveloperInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    val packageName: String,
    var developerInfo: DeveloperInfo = DeveloperInfo("unknown"),
    var artwork: Artwork = Artwork(),
    var privacyUrl: String = "",
    var entries: List<Entry> = listOf()
) : Parcelable {
    override fun hashCode(): Int {
        return packageName.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Report -> {
                packageName == other.packageName
            }

            else -> false
        }
    }
}
