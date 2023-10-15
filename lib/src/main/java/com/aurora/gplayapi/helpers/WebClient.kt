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

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import java.net.URLEncoder

class WebClient {

    fun fetch(rpcRequests: Array<String>): String {
        val url = "https://play.google.com/_/PlayStoreUi/data/batchexecute"
        val requestsBody = buildFRequest(rpcRequests)

        val request = Fuel.post(
            url,
            listOf()
        )
            .header("Content-Type" to "application/x-www-form-urlencoded;charset=utf-8")
            .header("Origin" to "https://play.google.com")
            .body(requestsBody)

        val (_, _, result) = request.responseString()

        return when (result) {
            is Result.Success -> result.get()
            is Result.Failure -> {
                throw result.error.exception
            }
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
