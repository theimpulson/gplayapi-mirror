/*
 *     GPlayApi
 *     Copyright (C) 2020  Aurora OSS
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
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
