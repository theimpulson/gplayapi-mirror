/*
 * SPDX-FileCopyrightText: 2024 Aurora OSS
 * SPDX-FileCopyrightText: 2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.data.builders.rpc.SearchQueryBuilder
import com.aurora.gplayapi.data.builders.rpc.SearchSuggestionQueryBuilder
import com.aurora.gplayapi.data.models.SearchBundle
import com.aurora.gplayapi.helpers.contracts.SearchContract
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig
import java.util.Locale
import java.util.UUID

class WebSearchHelper : BaseWebHelper(), SearchContract {
    private var query: String = String()

    override fun with(locale: Locale) = apply {
        this.locale = locale
    }

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    override fun searchSuggestions(query: String): List<SearchSuggestEntry> {
        val response = execute(SearchSuggestionQueryBuilder.build(query))

        val payload = response.dig<Collection<Any>>(
            SearchSuggestionQueryBuilder.TAG,
            query,
            0
        )

        if (payload.isNullOrEmpty()) {
            return emptyList()
        }

        val suggestions = payload.map {
            SearchSuggestEntry.newBuilder().apply {
                title = it.dig(0)
            }.build()
        }

        return suggestions
    }

    override fun searchResults(query: String, nextPageUrl: String): SearchBundle {
        this.query = query

        val response = execute(SearchQueryBuilder.build(query, nextPageUrl))

        var payload = response.dig<Collection<Any>>(
            SearchQueryBuilder.TAG,
            query,
            0
        )

        if (payload.isNullOrEmpty()) {
            return SearchBundle()
        }

        // First stream is search stream, following are app streams (made-up names :p)
        if (payload.dig<String>(0, 1) != "Apps") {
            payload = payload.dig(1, 0)
        }

        // Find only the package names, complete app info is fetched via AppDetailsHelper
        val packageNames: List<String> = payload?.dig<Collection<Any>>(0, 0)?.let { entry ->
            entry.mapNotNull {
                it.dig(12, 0)
            }
        } ?: emptyList()

        if (packageNames.isEmpty()) {
            return SearchBundle()
        }

        val nextPageToken: String = payload?.dig<String>(0, 7, 1) ?: ""

        // Include sub-bundles only if there is a next page
        return SearchBundle(
            id = UUID.randomUUID().hashCode(),
            appList = getAppDetails(packageNames),
            query = query,
            subBundles = if (nextPageToken.isNotEmpty()) {
                hashSetOf(SearchBundle.SubBundle(nextPageToken, SearchBundle.Type.GENERIC))
            } else {
                hashSetOf()
            }
        )
    }

    override fun next(bundleSet: MutableSet<SearchBundle.SubBundle>): SearchBundle {
        val compositeSearchBundle = SearchBundle()

        bundleSet.forEach {
            val searchBundle = searchResults(query, it.nextPageUrl)
            compositeSearchBundle.appList.addAll(searchBundle.appList)
            compositeSearchBundle.subBundles.addAll(searchBundle.subBundles)
        }

        return compositeSearchBundle
    }
}
