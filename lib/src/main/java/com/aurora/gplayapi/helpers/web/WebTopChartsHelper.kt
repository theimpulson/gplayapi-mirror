package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.rpc.TopChartsBuilder
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig
import java.util.UUID

class WebTopChartsHelper : BaseWebHelper() {
    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    /**
     * Fetches the top charts for a given category and chart
     * @param category The category of the top charts
     * @param chart The chart of the top charts
     * @return The stream cluster of the top charts
     * @throws Exception If an error occurs while fetching the top charts
     * @see StreamCluster
     *
     * Possible values for chart:
     * - topselling_paid
     * - topselling_free
     * - topselling_new_free
     * - topselling_new_paid
     * - topgrossing
     * - movers_shakers
     * */
    @Throws(Exception::class)
    fun getCluster(category: String, chart: String): StreamCluster {
        var webChart = chart

        // Remove apps_ prefix, web charts don't have it
        if (chart.startsWith("apps_")) {
            webChart = chart.substring(5)
        }

        val response = execute(arrayOf(TopChartsBuilder.build(category, webChart)))

        val payload = response.dig<Collection<Any>>(
            TopChartsBuilder.TAG,
            "$category$webChart"
        )

        val streamCluster = StreamCluster()
        if (payload.isNullOrEmpty()) {
            return streamCluster
        }

        val packageNames: List<String> =
            payload.dig<Collection<Any>>(0, 1, 0, 28, 0)?.let { entry ->
                entry.mapNotNull {
                    it.dig(0, 0, 0)
                }
            } ?: emptyList()

        if (packageNames.isEmpty()) {
            return streamCluster
        }

        val apps = getAppDetails(packageNames)

        streamCluster.apply {
            id = UUID.randomUUID().hashCode()
            clusterTitle = category
            clusterSubtitle = chart
            clusterAppList = apps
        }

        return streamCluster
    }
}
