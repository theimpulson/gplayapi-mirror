/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.details

import android.os.Parcelable
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.utils.Commons
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DevStream(
    val id: Int = Commons.getUniqueId(),
    val title: String = String(),
    val description: String = String(),
    val imgUrl: String = String(),
    val streamBundle: StreamBundle = StreamBundle.EMPTY
) : Parcelable {
    companion object {
        val EMPTY = DevStream()
    }

    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is DevStream -> {
                id == other.id
            }

            else -> false
        }
    }
}
