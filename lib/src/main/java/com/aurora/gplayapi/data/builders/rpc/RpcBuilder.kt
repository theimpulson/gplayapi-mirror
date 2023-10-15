package com.aurora.gplayapi.data.builders.rpc

import com.aurora.gplayapi.utils.dig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RpcBuilder {
    fun wrapResponse(input: String): HashMap<String, HashMap<String, Any>?> {
        val lines = input.lines()
        val filteredLines = lines.filter { it.startsWith("[[\"wrb.fr") }
        val result = HashMap<String, HashMap<String, Any>?>()

        filteredLines.forEach {
            val jaggedProto = parseJaggedString(it)
            val (type, packageName) = (jaggedProto.dig<String>(0, 6)).toString().split("@")

            result[type] = hashMapOf(
                packageName to parseJaggedString(jaggedProto.dig<String>(0, 2))
            )
        }

        return result
    }

    private fun parseJaggedString(input: String?): Collection<Any> {
        val gson = Gson()
        val arrayType = object : TypeToken<Collection<Any>>() {}.type
        return gson.fromJson(input, arrayType)
    }
}
