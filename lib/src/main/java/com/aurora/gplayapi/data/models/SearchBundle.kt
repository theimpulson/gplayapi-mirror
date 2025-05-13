/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SearchBundle(
    val id: Int = -1,
    val query: String = String(),
    val suggestionTerms: Set<String> = HashSet(),
    val subBundles: Set<SubBundle> = hashSetOf(),
    val appList: List<App> = listOf()
) : Parcelable {

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is SearchBundle -> {
                id == other.id
            }

            else -> false
        }
    }

    enum class Type {
        GENERIC, SIMILAR, RELATED_SEARCHES, RELATED_TO_YOUR_SEARCH, YOU_MIGHT_ALSO_LIKE, BOGUS
    }

    @Serializable
    @Parcelize
    data class SubBundle(val nextPageUrl: String, val type: Type) : Parcelable {

        override fun hashCode(): Int {
            return nextPageUrl.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return when (other) {
                is SubBundle -> nextPageUrl == other.nextPageUrl
                else -> false
            }
        }
    }
}
