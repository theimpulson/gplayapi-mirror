/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.Item
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.Category
import com.aurora.gplayapi.data.providers.HeaderProvider
import com.aurora.gplayapi.helpers.contracts.CategoryContract
import com.aurora.gplayapi.network.IHttpClient

class CategoryHelper(authData: AuthData) : NativeHelper(authData), CategoryContract {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @Throws(Exception::class)
    override fun getAllCategories(type: Category.Type): List<Category> {
        val headers = HeaderProvider.getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()
        params["c"] = "3"
        params["cat"] = type.value

        val playResponse = httpClient.get(GooglePlayApi.CATEGORIES_URL, headers, params)
        val listResponse: ListResponse = getResponseFromBytes(playResponse.responseBytes)

        with(listResponse) {
            if (hasItem()) {
                with(item) {
                    // Categories are nested items, so we need to iterate through them
                    // list -> item -> subitem[0] -> subitem -> category item
                    if (subItemList.isNotEmpty()) {
                        return subItemList.first().subItemList.map { si ->
                            getCategoryFromItem(type, si)
                        }
                    }
                }
            }
        }

        return emptyList()
    }

    private fun getCategoryFromItem(type: Category.Type, item: Item): Category {
        return Category(
            title = item.title,
            imageUrl = item.getImage(0).imageUrl,
            color = item.getImage(0).fillColorRGB,
            browseUrl = item.annotations.annotationLink.resolvedLink.browseUrl,
            type = type
        )
    }
}
