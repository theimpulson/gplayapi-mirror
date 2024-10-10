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
data class StreamCluster(
    val id: Int = Commons.getUniqueId(),
    var clusterTitle: String = "",
    var clusterSubtitle: String = "",
    var clusterNextPageUrl: String = "",
    var clusterBrowseUrl: String = "",
    var clusterAppList: MutableList<App> = mutableListOf()
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
