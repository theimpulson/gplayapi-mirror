/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.data.models.editor.EditorChoiceBundle
import com.aurora.gplayapi.data.providers.HeaderProvider.getDefaultHeaders
import com.aurora.gplayapi.helpers.contracts.StreamContract
import com.aurora.gplayapi.helpers.contracts.StreamContract.Category
import com.aurora.gplayapi.helpers.contracts.StreamContract.Type
import com.aurora.gplayapi.network.IHttpClient

class StreamHelper(authData: AuthData) : NativeHelper(authData), StreamContract {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    override fun fetch(type: Type, category: Category): StreamBundle {
        return getNavStream(type, category)
    }

    override fun nextStreamCluster(nextPageUrl: String): StreamCluster {
        return getNextStreamCluster(nextPageUrl)
    }

    override fun nextStreamBundle(category: Category, nextPageToken: String): StreamBundle {
        return next(nextPageToken)
    }

    @Throws(Exception::class)
    fun getNavStream(type: Type, category: Category): StreamBundle {
        val listResponse = getListResponse(type, category)
        return getStreamBundle(listResponse)
    }

    fun getEditorChoiceStream(category: Category): List<EditorChoiceBundle> {
        val listResponse = getListResponse(Type.EDITOR_CHOICE, category)
        return getEditorChoiceBundles(listResponse)
    }

    @Throws(Exception::class)
    fun next(nextPageUrl: String): StreamBundle {
        val listResponse = getNextStreamResponse(nextPageUrl)
        return getStreamBundle(listResponse)
    }

    @Throws(Exception::class)
    private fun getListResponse(type: Type, category: Category): ListResponse {
        val headers: MutableMap<String, String> = getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()
        params["c"] = "3"

        if (type == Type.EARLY_ACCESS) {
            params["ct"] = "1"
        } else {
            if (category != Category.NONE) {
                params["cat"] = category.value
            }
        }

        val playResponse =
            httpClient.get(GooglePlayApi.URL_FDFE + "/" + type.value, headers, params)

        return if (playResponse.isSuccessful) {
            getResponseFromBytes(playResponse.responseBytes)
        } else {
            ListResponse.getDefaultInstance()
        }
    }
}
