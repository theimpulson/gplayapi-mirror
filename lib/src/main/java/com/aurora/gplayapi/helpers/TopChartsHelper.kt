/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.data.providers.HeaderProvider.getDefaultHeaders
import com.aurora.gplayapi.helpers.contracts.TopChartsContract
import com.aurora.gplayapi.network.IHttpClient

class TopChartsHelper(authData: AuthData) : NativeHelper(authData), TopChartsContract {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @Throws(Exception::class)
    override fun getCluster(category: String, chart: String): StreamCluster {
        val headers: MutableMap<String, String> = getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()
        params["c"] = "3"
        params["stcid"] = chart
        params["scat"] = category

        val playResponse = httpClient.get(GooglePlayApi.TOP_CHART_URL, headers, params)
        return if (playResponse.isSuccessful) {
            val listResponse: ListResponse = getResponseFromBytes(playResponse.responseBytes)
            getStreamCluster(listResponse)
        } else {
            StreamCluster.EMPTY
        }
    }
}
