package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.rpc.FeaturedStreamBuilder
import com.aurora.gplayapi.data.builders.rpc.NextBundleBuilder
import com.aurora.gplayapi.data.builders.rpc.NextClusterBuilder
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig

class WebStreamHelper : BaseWebHelper() {
    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    /**
     * Fetches the stream cluster for the given category.
     * @param category The category to fetch the stream cluster for.
     * @return The stream bundle for the given category.
     * @see StreamBundle
     *
     * Possible categories:
     * - "APPLICATION" for Apps
     * - "GAME" for Games
     * + All known categories supported by Google Play Store
     */
    fun fetch(category: String): StreamBundle {
        val response = execute(arrayOf(FeaturedStreamBuilder.build(category)))
        val payload = response.dig<Collection<Any>>(
            FeaturedStreamBuilder.TAG,
            category
        )

        var streamBundle = StreamBundle()
        if (payload.isNullOrEmpty()) {
            return streamBundle
        }

        streamBundle = parseBundle(category, payload)

        return streamBundle
    }

    /**
     * Fetches the next stream of the cluster.
     * @param nextPageUrl The URL to fetch the next stream cluster from.
     * @return The next stream cluster.
     * @see StreamCluster
     */
    fun nextStreamCluster(nextPageUrl: String): StreamCluster {
        val response = execute(arrayOf(NextClusterBuilder.build(nextPageUrl)))

        val payload = response.dig<Collection<Any>>(
            NextClusterBuilder.TAG,
            nextPageUrl.hashCode().toString(),
            0 // For next stream cluster, the payload is a list of clusters even if it's just one cluster
        )

        var streamCluster = StreamCluster()

        if (payload.isNullOrEmpty()) {
            return streamCluster
        }

        streamCluster = parseCluster(payload, 21)

        return streamCluster
    }

    /**
     * Fetches the next stream of bundle.
     * @param category The category to fetch the next stream bundle for.
     * @param nextPageToken The token to fetch the next stream bundle from.
     * @return The next stream bundle.
     * @see StreamBundle
     */
    fun nextStreamBundle(category: String, nextPageToken: String): StreamBundle {
        val response = execute(arrayOf(NextBundleBuilder.build(category, nextPageToken)))

        val payload = response.dig<Collection<Any>>(
            NextBundleBuilder.TAG,
            category
        )

        var streamBundle = StreamBundle()
        if (payload.isNullOrEmpty()) {
            return streamBundle
        }

        streamBundle = parseBundle(category, payload)

        return streamBundle
    }
}
