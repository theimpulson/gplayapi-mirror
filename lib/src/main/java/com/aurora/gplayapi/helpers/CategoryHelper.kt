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

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.Item
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.Payload
import com.aurora.gplayapi.ResponseWrapper
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.Category
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.providers.HeaderProvider
import com.aurora.gplayapi.helpers.contracts.CategoryContract
import com.aurora.gplayapi.network.IHttpClient

class CategoryHelper(authData: AuthData) : NativeHelper(authData), CategoryContract {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @Throws(Exception::class)
    override fun getAllCategoriesList(type: Category.Type): List<Category> {
        val categoryList: MutableList<Category> = ArrayList()
        val headers = HeaderProvider.getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()
        params["c"] = "3"
        params["cat"] = type.value

        val playResponse = httpClient.get(GooglePlayApi.CATEGORIES_URL, headers, params)
        val listResponse: ListResponse = getResponseFromBytes(playResponse.responseBytes)

        if (listResponse.itemCount > 0) {
            val item = listResponse.getItem(0)
            if (item.subItemCount > 0) {
                val subItem = item.getSubItem(0)
                if (subItem.subItemCount > 0) {
                    for (subSubItem in subItem.subItemList) {
                        categoryList.add(getCategoryFromItem(type, subSubItem))
                    }
                }
            }
        }
        return categoryList
    }

    @Throws(Exception::class)
    override fun getSubCategoryBundle(homeUrl: String): StreamBundle {
        val headers = HeaderProvider.getDefaultHeaders(authData)
        val playResponse = httpClient.get(GooglePlayApi.URL_FDFE + "/" + homeUrl, headers)

        return getSubCategoryBundle(playResponse.responseBytes)
    }

    private fun getSubCategoryBundle(payload: Payload): StreamBundle {
        var streamBundle = StreamBundle()
        if (payload.hasListResponse() && payload.listResponse.itemCount > 0) {
            streamBundle = getStreamBundle(payload.listResponse)
        }
        return streamBundle
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

    private fun getCategoryFromItem(type: Category.Type, subItem: Item): Category {
        val category = Category()
        category.title = subItem.title
        category.imageUrl = subItem.getImage(0).imageUrl
        category.color = subItem.getImage(0).fillColorRGB
        category.browseUrl = subItem.annotations.annotationLink.resolvedLink.browseUrl
        category.type = type
        return category
    }
}
