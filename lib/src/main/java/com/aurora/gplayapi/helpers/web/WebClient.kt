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

package com.aurora.gplayapi.helpers.web

import android.util.Log
import com.aurora.gplayapi.network.DefaultHttpClient
import com.aurora.gplayapi.network.IHttpClient
import java.net.URLEncoder
import java.nio.charset.Charset

class WebClient(val httpClient: IHttpClient = DefaultHttpClient) {
    private val TAG = javaClass.simpleName

    fun fetch(rpcRequests: Array<String>): String {
        val url = "https://play.google.com/_/PlayStoreUi/data/batchexecute"
        val headers = mapOf(
            "Content-Type" to "application/x-www-form-urlencoded;charset=utf-8",
            "Origin" to "https://play.google.com"
        )

        return try {
            val payload = buildFRequest(rpcRequests)
            val response = httpClient.post(url, headers, payload)

            String(response.responseBytes)
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to fetch request", exception)
            String()
        }
    }

    private fun buildFRequest(rpcRequests: Array<String>): ByteArray {
        return """
            f.req=[[
                ${rpcRequests.joinToString(separator = ",") { URLEncoder.encode(it, "UTF-8") }}
            ]]
        """
            .trim()
            .trimIndent()
            .toByteArray(Charset.defaultCharset())
    }
}
