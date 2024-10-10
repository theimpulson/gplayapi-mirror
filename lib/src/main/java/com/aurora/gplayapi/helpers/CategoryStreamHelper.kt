/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.Payload
import com.aurora.gplayapi.ResponseWrapper
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.data.providers.HeaderProvider
import com.aurora.gplayapi.helpers.contracts.CategoryStreamContract
import com.aurora.gplayapi.helpers.contracts.StreamContract
import com.aurora.gplayapi.network.IHttpClient

class CategoryStreamHelper(authData: AuthData) : NativeHelper(authData), CategoryStreamContract {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @Throws(Exception::class)
    override fun fetch(url: String): StreamBundle {
        val headers = HeaderProvider.getDefaultHeaders(authData)
        val playResponse = httpClient.get(GooglePlayApi.URL_FDFE + "/" + url, headers)

        return getSubCategoryBundle(playResponse.responseBytes)
    }

    override fun nextStreamCluster(nextPageUrl: String): StreamCluster {
        return getNextStreamCluster(nextPageUrl)
    }

    override fun nextStreamBundle(
        category: StreamContract.Category,
        nextPageToken: String
    ): StreamBundle {
        return fetch(nextPageToken)
    }

    @Throws(Exception::class)
    private fun getSubCategoryBundle(bytes: ByteArray?): StreamBundle {
        val responseWrapper = ResponseWrapper.parseFrom(bytes)
        var streamBundle = StreamBundle()

        if (responseWrapper.preFetchCount > 0) {
            responseWrapper.preFetchList.forEach {
                if (it.hasResponse() && it.response.hasPayload()) {
                    val payload = it.response.payload
                    val currentStreamBundle = getSubCategoryBundle(payload)
                    streamBundle.streamClusters.putAll(currentStreamBundle.streamClusters)
                }
            }
        } else if (responseWrapper.hasPayload()) {
            val payload = responseWrapper.payload
            streamBundle = getSubCategoryBundle(payload)
        }

        return streamBundle
    }

    private fun getSubCategoryBundle(payload: Payload): StreamBundle {
        var streamBundle = StreamBundle()
        if (payload.hasListResponse() && payload.listResponse.itemCount > 0) {
            streamBundle = getStreamBundle(payload.listResponse)
        }
        return streamBundle
    }
}
