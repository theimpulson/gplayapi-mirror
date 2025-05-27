/*
 * SPDX-FileCopyrightText: 2024 Aurora OSS
 * SPDX-FileCopyrightText: 2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.BaseHelper
import com.aurora.gplayapi.utils.Commons
import com.aurora.gplayapi.utils.dig
import java.util.Locale

abstract class BaseWebHelper : BaseHelper() {

    var locale: Locale = Locale.getDefault()

    abstract fun with(locale: Locale): BaseWebHelper

    fun execute(freq: String): HashMap<String, HashMap<String, Any>> {
        val response = WebClient(httpClient, locale).fetch(listOf(freq))

        return RpcBuilder.wrapResponse(response)
    }

    fun parseBundle(category: String, payload: Any): StreamBundle {
        var streamBundle = StreamBundle()
        payload.dig<List<Any>>(0, 1).let { entries ->
            entries.mapNotNull { entry ->
                val topCharts = entry.dig<List<Any>>(27, 1)
                val events = entry.dig<List<Any>>(34, 0)
                val banner = entry.dig<List<Any>>(29, 0)
                val legacyCluster = entry.dig<List<Any>>(21, 0)
                val cluster = entry.dig<List<Any>>(22, 0)

                // Skip events and top charts clusters
                // TODO: Add support for events in the future
                if (events.isNotEmpty() || topCharts.isNotEmpty()) {
                    return@mapNotNull null
                }

                if (legacyCluster.isNotEmpty()) {
                    return@mapNotNull parseCluster(entry, 21)
                }

                if (banner.isNotEmpty()) {
                    return@mapNotNull parseCluster(entry, 29)
                }

                if (cluster.isNotEmpty()) {
                    return@mapNotNull parseCluster(entry, 22, listOf(0, 0, 0))
                }

                StreamCluster.EMPTY
            }
                .filter { it.clusterAppList.isNotEmpty() }
                .also {
                    streamBundle = StreamBundle(
                        id = Commons.getUniqueId(),
                        streamTitle = category,
                        streamNextPageUrl = payload.dig<String>(0, 3, 1),
                        streamClusters = it.associateBy { b -> b.id }.toMutableMap()
                    )
                }
        }
        return streamBundle
    }

    fun parseCluster(
        payload: Any,
        clusterIndex: Int,
        appIndex: List<Int> = listOf(0, 0)
    ): StreamCluster {
        return StreamCluster(
            clusterTitle = payload.dig<String>(clusterIndex, 1, 0),
            clusterSubtitle = payload.dig<String>(clusterIndex, 1, 1),
            clusterNextPageUrl = payload.dig<String>(clusterIndex, 1, 3, 1),
            clusterBrowseUrl = payload.dig<String>(clusterIndex, 1, 2, 4, 2),
            clusterAppList = getAppDetails(
                payload.dig<List<Any>>(clusterIndex, 0).mapNotNull {
                    it.dig(
                        *appIndex.toTypedArray()
                    )
                }
            )
        )
    }

    fun getAppDetails(packageNames: List<String>): MutableList<App> {
        return WebAppDetailsHelper()
            .with(locale)
            .using(httpClient)
            .getAppByPackageName(packageNames)
            .toMutableList()
    }
}
