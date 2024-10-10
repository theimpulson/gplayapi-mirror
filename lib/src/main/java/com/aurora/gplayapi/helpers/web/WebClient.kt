/*
 * SPDX-FileCopyrightText: 2024 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
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
