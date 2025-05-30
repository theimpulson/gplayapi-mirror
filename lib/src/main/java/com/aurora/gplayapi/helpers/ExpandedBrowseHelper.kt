/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.Item
import com.aurora.gplayapi.data.builders.AppBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.network.IHttpClient

class ExpandedBrowseHelper(authData: AuthData) : NativeHelper(authData) {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    fun getExpandedBrowseClusters(expandedBrowseUrl: String): StreamCluster {
        val listResponse = getNextStreamResponse(expandedBrowseUrl)
        return getStreamCluster(listResponse.item)
    }

    override fun getAppsFromItem(item: Item): MutableList<App> {
        val appList: MutableList<App> = mutableListOf()
        item.subItemList.forEach {
            it?.let {
                it.subItemList.forEach {
                    it?.let {
                        appList.add(AppBuilder.build(it))
                    }
                }
            }
        }
        return appList
    }
}
