/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artwork(
    var type: Int = 0,
    var url: String = String(),
    var urlAlt: String = String(),
    var aspectRatio: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) : Parcelable {

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Artwork -> url == other.url
            else -> false
        }
    }
}
