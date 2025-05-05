/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.BrowseResponse
import com.aurora.gplayapi.DetailsResponse
import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.Item
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.Payload
import com.aurora.gplayapi.PayloadApi
import com.aurora.gplayapi.ResponseWrapper
import com.aurora.gplayapi.ResponseWrapperApi
import com.aurora.gplayapi.SearchResponse
import com.aurora.gplayapi.SearchSuggestResponse
import com.aurora.gplayapi.data.builders.AppBuilder.build
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.Artwork
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.PlayResponse
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.data.models.editor.EditorChoiceBundle
import com.aurora.gplayapi.data.models.editor.EditorChoiceCluster
import com.aurora.gplayapi.data.providers.HeaderProvider.getDefaultHeaders
import java.io.IOException

abstract class NativeHelper(protected var authData: AuthData) : BaseHelper() {
    @Throws(IOException::class)
    fun getResponse(
        url: String,
        params: Map<String, String>,
        headers: Map<String, String>
    ): PlayResponse {
        return httpClient.get(url, headers, params)
    }

    /*-------------------------------------------- COMMONS -------------------------------------------------*/
    fun getNextPageUrl(item: Item): String {
        return if (item.hasContainerMetadata() && item.containerMetadata.hasNextPageUrl()) item.containerMetadata.nextPageUrl else String()
    }

    fun getBrowseUrl(item: Item): String {
        return if (item.hasContainerMetadata() && item.containerMetadata.hasBrowseUrl()) item.containerMetadata.browseUrl else String()
    }

    @Throws(Exception::class)
    fun getPayLoadFromBytes(bytes: ByteArray?): Payload {
        val responseWrapper = ResponseWrapper.parseFrom(bytes)
        return responseWrapper.payload ?: Payload.getDefaultInstance()
    }

    @Throws(Exception::class)
    fun getUserProfileResponse(bytes: ByteArray?): PayloadApi {
        val responseWrapper = ResponseWrapperApi.parseFrom(bytes)
        return responseWrapper.payload ?: PayloadApi.getDefaultInstance()
    }

    @Throws(Exception::class)
    inline fun <reified T> getResponseFromBytes(bytes: ByteArray?): T {
        val payload = getPayLoadFromBytes(bytes)

        return when (T::class) {
            BrowseResponse::class -> payload.browseResponse as T
            DetailsResponse::class -> payload.detailsResponse as T
            ListResponse::class -> payload.listResponse as T
            SearchResponse::class -> payload.searchResponse as T
            SearchSuggestResponse::class -> payload.searchSuggestResponse as T
            else -> null as T
        }
    }

    @Throws(Exception::class)
    fun getPrefetchPayLoad(bytes: ByteArray?): Payload {
        val responseWrapper = ResponseWrapper.parseFrom(bytes)
        val payload = responseWrapper.payload
        return if (responseWrapper.preFetchCount > 0 && (
                    (payload.hasSearchResponse() && payload.searchResponse.itemCount == 0) ||
                            payload.hasListResponse() && payload.listResponse.itemCount == 0 ||
                            payload.hasBrowseResponse())
        ) {
            responseWrapper.getPreFetch(0).response.payload
        } else {
            payload
        }
    }

    open fun getAppsFromItem(item: Item): MutableList<App> {
        val appList: MutableList<App> = mutableListOf()
        if (item.subItemCount > 0) {
            for (subItem in item.subItemList) {
                if (subItem.type == 1) {
                    val app = build(subItem)
                    appList.add(app)
                    // System.out.printf("%s -> %s\n", app.displayName, app.packageName);
                }
            }
        }
        return appList
    }

    /*--------------------------------------- GENERIC APP STREAMS --------------------------------------------*/
    @Throws(Exception::class)
    fun getNextStreamResponse(nextPageUrl: String): ListResponse {
        val headers: Map<String, String> = getDefaultHeaders(authData)
        val playResponse = httpClient.get(GooglePlayApi.URL_FDFE + "/" + nextPageUrl, headers)
        return if (playResponse.isSuccessful) {
            getResponseFromBytes(playResponse.responseBytes)
        } else {
            ListResponse.getDefaultInstance()
        }
    }

    @Throws(Exception::class)
    fun getBrowseStreamResponse(browseUrl: String): BrowseResponse {
        val headers: Map<String, String> = getDefaultHeaders(authData)
        val playResponse = httpClient.get(GooglePlayApi.URL_FDFE + "/" + browseUrl, headers)
        return if (playResponse.isSuccessful) {
            getResponseFromBytes(playResponse.responseBytes)
        } else {
            BrowseResponse.getDefaultInstance()
        }
    }

    @Throws(Exception::class)
    fun getNextStreamCluster(nextPageUrl: String): StreamCluster {
        val listResponse = getNextStreamResponse(nextPageUrl)
        return getStreamCluster(listResponse)
    }

    open fun getStreamCluster(item: Item): StreamCluster {
        val title = if (item.hasTitle()) item.title else String()
        val subtitle = if (item.hasSubtitle()) item.subtitle else String()
        val browseUrl = getBrowseUrl(item)

        return StreamCluster(
            clusterTitle = title,
            clusterSubtitle = subtitle,
            clusterBrowseUrl = browseUrl,
            clusterNextPageUrl = getNextPageUrl(item),
            clusterAppList = getAppsFromItem(item)
        )
    }

    fun getStreamCluster(payload: Payload): StreamCluster {
        return if (payload.hasListResponse()) {
            getStreamCluster(payload.listResponse)
        } else {
            StreamCluster()
        }
    }

    fun getStreamCluster(listResponse: ListResponse): StreamCluster {
        if (listResponse.itemCount > 0) {
            val item = listResponse.getItem(0)
            if (item != null && item.subItemCount > 0) {
                val subItem = item.getSubItem(0)
                return getStreamCluster(subItem)
            }
        }
        return StreamCluster()
    }

    fun getStreamClusters(listResponse: ListResponse): List<StreamCluster> {
        val streamClusters: MutableList<StreamCluster> = ArrayList()
        if (listResponse.itemCount > 0) {
            val item = listResponse.getItem(0)
            if (item != null && item.subItemCount > 0) {
                for (subItem in item.subItemList) {
                    streamClusters.add(getStreamCluster(subItem))
                }
            }
        }
        return streamClusters
    }

    fun getStreamBundle(listResponse: ListResponse): StreamBundle {
        var nextPageUrl = String()
        var title = String()
        val streamClusterMap: MutableMap<Int, StreamCluster> = mutableMapOf()

        if (listResponse.itemCount > 0) {
            val item = listResponse.getItem(0)
            if (item != null && item.subItemCount > 0) {
                for (subItem in item.subItemList) {
                    val streamCluster = getStreamCluster(subItem)
                    streamClusterMap[streamCluster.id] = streamCluster
                }
                title = item.title
                nextPageUrl = getNextPageUrl(item)
            }
        }
        return StreamBundle(
            streamTitle = title,
            streamNextPageUrl = nextPageUrl,
            streamClusters = streamClusterMap
        )
    }

    /*------------------------------------- EDITOR'S CHOICE CLUSTER & BUNDLES ------------------------------------*/
    @Throws(Exception::class)
    private fun getEditorChoiceCluster(item: Item): EditorChoiceCluster {
        val title = if (item.hasTitle()) item.title else String()
        val artworkList: MutableList<Artwork> = ArrayList()
        val browseUrl = getBrowseUrl(item)
        if (item.imageCount > 0) {
            item.imageList.forEach {
                artworkList.add(
                    Artwork(
                        type = it.imageType,
                        url = it.imageUrl,
                        aspectRatio = it.dimension.aspectRatio,
                        width = it.dimension.width,
                        height = it.dimension.height
                    )
                )
            }
        }
        return EditorChoiceCluster(
            id = browseUrl.hashCode(),
            clusterTitle = title,
            clusterBrowseUrl = browseUrl,
            clusterArtwork = artworkList
        )
    }

    private fun getEditorChoiceBundles(item: Item): EditorChoiceBundle {
        val title = if (item.hasTitle()) item.title else String()
        val choiceClusters: MutableList<EditorChoiceCluster> = ArrayList()
        for (subItem in item.subItemList) {
            choiceClusters.add(getEditorChoiceCluster(subItem))
        }

        return EditorChoiceBundle(
            id = title.hashCode(),
            bundleTitle = title,
            bundleChoiceClusters = choiceClusters
        )
    }

    fun getEditorChoiceBundles(listResponse: ListResponse?): List<EditorChoiceBundle> {
        val editorChoiceBundles: MutableList<EditorChoiceBundle> = ArrayList()

        listResponse?.itemList?.forEach { item ->
            item?.subItemList?.forEach { subItem ->
                subItem?.let {
                    val bundle = getEditorChoiceBundles(it)
                    if (bundle.bundleChoiceClusters.isNotEmpty()) {
                        editorChoiceBundles.add(bundle)
                    }
                }
            }
        }

        return editorChoiceBundles
    }
}
