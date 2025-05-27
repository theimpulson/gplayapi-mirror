/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import android.annotation.SuppressLint
import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.Item
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.SearchSuggestResponse
import com.aurora.gplayapi.data.builders.AppBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.PlayResponse
import com.aurora.gplayapi.data.models.SearchBundle
import com.aurora.gplayapi.data.models.SearchBundle.SubBundle
import com.aurora.gplayapi.data.providers.HeaderProvider.getDefaultHeaders
import com.aurora.gplayapi.helpers.contracts.SearchContract
import com.aurora.gplayapi.network.IHttpClient

class SearchHelper(authData: AuthData) : NativeHelper(authData), SearchContract {

    private val searchTypeExtra = "_-"

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    private fun getSubBundle(item: Item): SubBundle {
        try {
            val nextPageUrl = item.containerMetadata.nextPageUrl
            if (nextPageUrl.isNotBlank()) {
                if (nextPageUrl.contains(searchTypeExtra)) {
                    if (nextPageUrl.startsWith("getCluster?enpt=CkC")) {
                        return SubBundle(nextPageUrl, SearchBundle.Type.SIMILAR)
                    }
                    if (nextPageUrl.startsWith("getCluster?enpt=CkG")) {
                        return SubBundle(nextPageUrl, SearchBundle.Type.RELATED_TO_YOUR_SEARCH)
                    }
                } else {
                    return SubBundle(nextPageUrl, SearchBundle.Type.GENERIC)
                }
            }
        } catch (ignored: Exception) {
        }
        return SubBundle("", SearchBundle.Type.BOGUS)
    }

    private var query: String = String()

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
    override fun searchResults(query: String, nextPageUrl: String): SearchBundle {
        this.query = query
        val header: MutableMap<String, String> = getDefaultHeaders(authData)
        val param: MutableMap<String, String> = HashMap()
        param["q"] = query
        param["c"] = "3"
        param["ksm"] = "1"

        val responseBody: PlayResponse = if (nextPageUrl.isNotEmpty()) {
            httpClient.get(GooglePlayApi.URL_SEARCH + "/" + nextPageUrl, header)
        } else {
            httpClient.get(GooglePlayApi.URL_SEARCH, header, param)
        }

        var searchBundle = SearchBundle()

        if (responseBody.isSuccessful) {
            val payload = getPrefetchPayLoad(responseBody.responseBytes)
            if (payload.hasListResponse()) {
                searchBundle = getSearchBundle(payload.listResponse).copy(
                    subBundles = searchBundle.subBundles
                        .filter { it.type == SearchBundle.Type.GENERIC }
                        .toMutableSet()
                )
                return searchBundle
            }
        }

        return searchBundle
    }

    @Throws(Exception::class)
    override fun next(bundleSet: MutableSet<SubBundle>): SearchBundle {
        val appList = mutableListOf<App>()
        val subBundles = mutableSetOf<SubBundle>()

        bundleSet.forEach {
            val searchBundle = searchResults(query, it.nextPageUrl)
            appList.addAll(searchBundle.appList)
            subBundles.addAll(searchBundle.subBundles)
        }

        return SearchBundle(appList = appList, subBundles = subBundles)
    }

    private fun getSearchBundle(listResponse: ListResponse): SearchBundle {
        val appList = mutableListOf<App>()
        val subBundles = mutableSetOf<SubBundle>()
        val itemList = listResponse.itemList

        itemList.forEach { item ->
            if (item.subItemCount > 0) {
                for (subItem in item.subItemList) {
                    // Filter out only apps, discard other items (Music, Ebooks, Movies)
                    if (subItem.type == 45) {
                        if (subItem.title.isEmpty() || subItem.title == "Apps") {
                            appList.addAll(getAppsFromItem(subItem))
                        } else {
                            if (subItem.title.isNotEmpty()) {
                                continue // Filter out `You Might Also Like` & `Related Apps`
                            }
                            appList.add(AppBuilder.build(subItem))
                        }
                    }
                    subBundles.add(getSubBundle(subItem))
                }
                subBundles.add(getSubBundle(item))
            }
        }
        return SearchBundle(appList = appList, subBundles = subBundles)
    }
}
