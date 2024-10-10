/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import com.aurora.gplayapi.utils.Commons
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamBundle(
    val id: Int = Commons.getUniqueId(),
    var streamTitle: String = String(),
    var streamNextPageUrl: String = String(),
    var streamClusters: MutableMap<Int, StreamCluster> = mutableMapOf()
) : Parcelable {
    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is StreamBundle -> {
                id == other.id
            }

            else -> false
        }
    }

    operator fun hasNext(): Boolean {
        return streamNextPageUrl.isNotBlank()
    }

    fun hasCluster(): Boolean {
        return streamClusters.isNotEmpty()
    }
}
