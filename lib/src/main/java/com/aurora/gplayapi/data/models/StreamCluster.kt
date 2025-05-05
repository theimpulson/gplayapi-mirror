/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import com.aurora.gplayapi.utils.Commons
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamCluster(
    val id: Int = Commons.getUniqueId(),
    val clusterTitle: String = String(),
    val clusterSubtitle: String = String(),
    val clusterNextPageUrl: String = String(),
    val clusterBrowseUrl: String = String(),
    val clusterAppList: List<App> = listOf()
) : Parcelable {
    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is StreamCluster -> {
                id == other.id
            }

            else -> false
        }
    }

    fun hasNext(): Boolean {
        return clusterNextPageUrl.isNotBlank()
    }
}
