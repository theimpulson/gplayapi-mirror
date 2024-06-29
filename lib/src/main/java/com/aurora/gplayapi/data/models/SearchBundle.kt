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

data class SearchBundle(
    val id: Int = -1,
    val query: String = String(),
    val suggestionTerms: MutableSet<String> = HashSet(),
    val subBundles: MutableSet<SubBundle> = hashSetOf(),
    val appList: MutableList<App> = mutableListOf()
) {

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

    data class SubBundle(val nextPageUrl: String, val type: Type) {

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
