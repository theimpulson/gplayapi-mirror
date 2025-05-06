/*
 * SPDX-FileCopyrightText: 2023-2024 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders.rpc

import com.aurora.gplayapi.utils.dig
import kotlinx.serialization.json.Json

internal object RpcBuilder {
    fun wrapResponse(input: String): HashMap<String, HashMap<String, Any>> {
        val lines = input.lines()
        val filteredLines: ArrayList<Any> = arrayListOf()
        val result = HashMap<String, HashMap<String, Any>>()

        lines
            .filter { it.startsWith("[[\"wrb.fr") }
            .map { Json.decodeFromString<Collection<Any>>(it) }
            .first()
            .forEach {
                if (it.dig<String>(0) == "wrb.fr") {
                    filteredLines.add(it)
                }
            }

        filteredLines
            .forEach {
                val (type, packageName) = (it.dig<String>(6)).toString().split("@")
                val rpcData = it.dig<String>(2) ?: return@forEach

                if (result[type] == null) {
                    result[type] = hashMapOf()
                }

                result[type]?.put(packageName, Json.decodeFromString<Collection<Any>>(rpcData))
            }

        return result
    }
}
