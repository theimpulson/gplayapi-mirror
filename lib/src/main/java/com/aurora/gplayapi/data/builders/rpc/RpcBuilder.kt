/*
 * SPDX-FileCopyrightText: 2023-2024 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders.rpc

import com.aurora.gplayapi.utils.dig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal object RpcBuilder {

    private val gson = Gson()

    fun wrapResponse(input: String): HashMap<String, HashMap<String, Any>> {
        val lines = input.lines()
        val filteredLines: ArrayList<Any> = arrayListOf()
        val result = HashMap<String, HashMap<String, Any>>()

        lines
            .filter { it.startsWith("[[\"wrb.fr") }
            .map { parseJaggedString(it) }
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

                result[type]?.put(packageName, parseJaggedString(rpcData))
            }

        return result
    }

    private fun parseJaggedString(input: String): Collection<Any> {
        val arrayType = object : TypeToken<Collection<Any>>() {}.type
        return gson.fromJson(input, arrayType)
    }
}
