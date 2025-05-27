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
        val categoryList: MutableList<Category> = mutableListOf()
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

    private fun getCategoryFromItem(type: Category.Type, subItem: Item): Category {
        return Category(
            title = subItem.title,
            imageUrl = subItem.getImage(0).imageUrl,
            color = subItem.getImage(0).fillColorRGB,
            browseUrl = subItem.annotations.annotationLink.resolvedLink.browseUrl,
            type = type
        )
    }
}
