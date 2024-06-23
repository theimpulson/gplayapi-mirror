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
            StreamCluster()
        }
    }
}
