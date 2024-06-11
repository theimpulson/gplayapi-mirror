package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.AppDetailsHelper
import com.aurora.gplayapi.network.DefaultHttpClient
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig
import java.util.UUID

open class BaseWebHelper(var authData: AuthData) {
    private var httpClient: IHttpClient = DefaultHttpClient
    private val appDetailsHelper = AppDetailsHelper(authData)

    fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    fun execute(freq: Array<String>): HashMap<String, HashMap<String, Any>?> {
        return WebClient(httpClient)
            .fetch(freq)
            .let {
                RpcBuilder.wrapResponse(it)
            }
    }

    fun parseBundle(category: String, payload: Any): StreamBundle {
        val streamBundle = StreamBundle()
        payload.dig<Collection<Any>>(0, 1)?.let { entries ->
            entries.mapNotNull { entry ->
                val topCharts = entry.dig(27, 1) ?: arrayListOf<Any>()
                val events = entry.dig(34, 0) ?: arrayListOf<Any>()
                val banner = entry.dig(29, 0) ?: arrayListOf<Any>()
                val legacyCluster = entry.dig(21, 0) ?: arrayListOf<Any>()
                val cluster = entry.dig(22, 0) ?: arrayListOf<Any>()

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
                    return@mapNotNull parseCluster(entry, 22, arrayOf(0, 0, 0))
                }

                StreamCluster()
            }
                .filter { it.clusterAppList.isNotEmpty() }
                .forEach { streamCluster ->
                    streamBundle.streamClusters[streamCluster.id] = streamCluster
                }
        }

        streamBundle.apply {
            id = UUID.randomUUID().hashCode()
            streamTitle = category
            streamNextPageUrl = payload.dig(0, 3, 1) ?: ""
        }

        return streamBundle
    }

    fun parseCluster(
        payload: Any,
        clusterIndex: Int,
        appIndex: Array<Int> = arrayOf(0, 0)
    ): StreamCluster {
        return StreamCluster().apply {
            id = UUID.randomUUID().hashCode()
            clusterTitle = payload.dig(clusterIndex, 1, 0) ?: ""
            clusterSubtitle = payload.dig(clusterIndex, 1, 1) ?: ""
            clusterNextPageUrl = payload.dig(clusterIndex, 1, 3, 1) ?: ""
            clusterBrowseUrl = payload.dig(clusterIndex, 1, 2, 4, 2) ?: ""
            clusterAppList = getAppByPackageName(
                (payload.dig<ArrayList<Any>>(clusterIndex, 0) ?: arrayListOf()).mapNotNull {
                    it.dig(
                        *appIndex
                    )
                }
            )
        }
    }

    /**
     * TODO: Parse the payload to get the app details instead of fetching via AppDetailsHelper
     */
    fun getAppByPackageName(packageNames: List<String>): MutableList<App> {
        return appDetailsHelper.using(httpClient)
            .getAppByPackageName(packageNames)
            .filter { it.packageName.isNotEmpty() }
            .toMutableList()
    }
}
