/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.details

import android.os.Parcelable
import com.aurora.gplayapi.data.models.Artwork
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Badge(
    var id: String = UUID.randomUUID().toString(),
    var textMajor: String = String(),
    var textMinor: String = String(),
    var textMinorHtml: String? = String(),
    var textDescription: String? = String(),
    var artwork: Artwork? = null,
    var link: String = String()
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Badge -> other.id == id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
