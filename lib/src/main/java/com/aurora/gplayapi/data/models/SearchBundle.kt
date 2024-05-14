/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

class SearchBundle {
    var id: Int = -1
    var query: String = String()
    var suggestionTerms: MutableSet<String> = HashSet()
    var subBundles: MutableSet<SubBundle> = hashSetOf()
    var appList: MutableList<App> = mutableListOf()

    enum class Type {
        GENERIC, SIMILAR, RELATED_SEARCHES, RELATED_TO_YOUR_SEARCH, YOU_MIGHT_ALSO_LIKE, BOGUS
    }

    class SubBundle(var nextPageUrl: String, var type: Type) {
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
