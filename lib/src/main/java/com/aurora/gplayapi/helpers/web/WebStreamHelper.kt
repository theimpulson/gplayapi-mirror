package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.rpc.FeaturedStreamBuilder
import com.aurora.gplayapi.data.builders.rpc.NextBundleBuilder
import com.aurora.gplayapi.data.builders.rpc.NextClusterBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.AppDetailsHelper
import com.aurora.gplayapi.utils.dig
import java.util.UUID

class WebStreamHelper(authData: AuthData) : BaseWebHelper(authData) {
    private val appDetailsHelper = AppDetailsHelper(authData)

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
