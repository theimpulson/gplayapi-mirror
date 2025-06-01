/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import android.annotation.SuppressLint
import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.SearchSuggestResponse
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.PlayResponse
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.data.providers.HeaderProvider.getDefaultHeaders
import com.aurora.gplayapi.helpers.contracts.SearchContract
import com.aurora.gplayapi.network.IHttpClient

class SearchHelper(authData: AuthData) : NativeHelper(authData), SearchContract {
    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @SuppressLint("DefaultLocale")
    @Throws(Exception::class)
    override fun searchSuggestions(query: String): List<SearchSuggestEntry> {
        val header: MutableMap<String, String> = getDefaultHeaders(authData)
        val paramString = String.format(
            "?q=%s&sb=%d&sst=%d&sst=%d",
            query,
            5,
            2 /*Text Entry*/,
            3 /*Item Doc Id : 3 -> Apps*/
        )
        val responseBody = httpClient.get(GooglePlayApi.URL_SEARCH_SUGGEST, header, paramString)
        val searchSuggestResponse: SearchSuggestResponse =
            getResponseFromBytes(responseBody.responseBytes)
        return if (searchSuggestResponse.entryCount > 0) {
            searchSuggestResponse.entryList
        } else {
            ArrayList()
        }
    }

    @Throws(Exception::class)
    override fun searchResults(query: String, nextPageUrl: String): StreamBundle {
        val headers: MutableMap<String, String> = getDefaultHeaders(authData)
        val param: MutableMap<String, String> = HashMap()
        param["q"] = query
        param["c"] = "3"
        param["ksm"] = "1"

        val responseBody: PlayResponse = if (nextPageUrl.isNotEmpty()) {
            httpClient.get("${GooglePlayApi.URL_FDFE}/$nextPageUrl", headers)
        } else {
            httpClient.get(GooglePlayApi.URL_SEARCH, headers, param)
        }

        return if (responseBody.isSuccessful) {
            val payload = getPrefetchPayLoad(responseBody.responseBytes)
            val stream = getStreamBundle(payload.listResponse)
            // Search stream title is set to the query
            stream.copy(streamTitle = query)
        } else {
            StreamBundle.EMPTY
        }
    }

    override fun nextStreamBundle(query: String, nextPageUrl: String): StreamBundle {
        return searchResults(query, nextPageUrl)
    }

    override fun nextStreamCluster(query: String, nextPageUrl: String): StreamCluster {
        return getNextStreamCluster(nextPageUrl)
    }
}
