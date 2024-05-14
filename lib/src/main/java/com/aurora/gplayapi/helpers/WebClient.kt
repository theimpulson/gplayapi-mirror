/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import android.util.Log
import com.aurora.gplayapi.network.DefaultHttpClient.POST
import com.aurora.gplayapi.network.DefaultHttpClient.okHttpClient
import java.net.URLEncoder
import okhttp3.Headers.Companion.toHeaders
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class WebClient {

    private val TAG = WebClient::class.java.simpleName

    fun fetch(rpcRequests: Array<String>): String {
        val url = "https://play.google.com/_/PlayStoreUi/data/batchexecute"
        val headersList = mapOf(
            "Content-Type" to "application/x-www-form-urlencoded;charset=utf-8",
            "Origin" to "https://play.google.com"
        )

        val request = Request.Builder()
            .url(url)
            .headers(headersList.toHeaders())
            .method(POST, buildFRequest(rpcRequests).toRequestBody())
            .build()

        return try {
            val response = okHttpClient.newCall(request).execute()
            response.body!!.string()
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to fetch request", exception)
            String()
        }
    }

    private fun buildFRequest(rpcRequests: Array<String>): String {
        return """
            f.req=[[
                ${rpcRequests.joinToString(separator = ",") { URLEncoder.encode(it, "UTF-8") }}
            ]]
        """
            .trim()
    }
}
