package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.Constants
import com.aurora.gplayapi.data.builders.WebAppBuilder
import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.builders.rpc.SearchQueryBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.utils.dig

class WebSearchHelper {
    fun searchResults(query: String): MutableMap<Constants.COLLECTION, List<App>> {
        val searchResponse = WebClient().fetch(
            arrayOf(
                SearchQueryBuilder.build(query)
            )
        ).let {
            RpcBuilder.wrapResponse(it)
        }

        val searchResults = Constants.COLLECTION.values().associateWith { enumValue ->
            when (enumValue) {
                Constants.COLLECTION.EXACT -> searchResponse.dig<Collection<Any>>(
                    SearchQueryBuilder.TAG,
                    query,
                    *enumValue.value
                )

                Constants.COLLECTION.QUERY -> searchResponse.dig<Collection<Any>>(
                    SearchQueryBuilder.TAG,
                    query,
                    *enumValue.value
                )

                Constants.COLLECTION.SIMILAR -> searchResponse.dig<Collection<Any>>(
                    SearchQueryBuilder.TAG,
                    query,
                    *enumValue.value
                ).map { it.dig<Collection<Any>>(0) }
            }
        }

        val apps = searchResults.mapValues { (key, value) ->
            if (key == Constants.COLLECTION.EXACT) {
                listOf(WebAppBuilder.buildExactApp(value))
            } else {
                value.map { app -> WebAppBuilder.build(app) }
            }
        }.toMutableMap()

        return apps
    }
}
