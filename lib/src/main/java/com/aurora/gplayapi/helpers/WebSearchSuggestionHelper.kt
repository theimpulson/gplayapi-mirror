package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.builders.rpc.SearchSuggestionQueryBuilder
import com.aurora.gplayapi.utils.dig

class WebSearchSuggestionHelper {
    fun searchSuggestions(query: String): List<SearchSuggestEntry> {
        val searchResponse = WebClient().fetch(
            arrayOf(
                SearchSuggestionQueryBuilder.build(query)
            )
        ).let {
            RpcBuilder.wrapResponse(it)
        }

        val suggestions = searchResponse.dig<Collection<Any>>(
            SearchSuggestionQueryBuilder.TAG,
            query,
            0
        ).map {
            SearchSuggestEntry.newBuilder().apply {
                title = it.dig(0)
            }.build()
        }

        return suggestions
    }
}
