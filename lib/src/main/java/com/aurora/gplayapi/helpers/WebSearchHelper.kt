package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.builders.rpc.SearchQueryBuilder
import com.aurora.gplayapi.data.builders.rpc.SearchSuggestionQueryBuilder
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.SearchBundle
import com.aurora.gplayapi.utils.dig

class WebSearchHelper(authData: AuthData) : SearchHelper(authData) {
    @Throws(Exception::class)
    override fun searchResults(query: String, nextPageUrl: String): SearchBundle {
        val searchResponse = WebClient().fetch(
            arrayOf(
                SearchQueryBuilder.build(query)
            )
        ).let {
            RpcBuilder.wrapResponse(it)
        }

        val packageNames = searchResponse.dig<Collection<Any>>(
            SearchQueryBuilder.TAG,
            query,
            0,
            1,
            0,
            0,
            0
        ).map { it.dig<String>(12, 0) }

        // TODO:: Figure out how to use this nextPageURL
        val nextPageUrl = searchResponse.dig<String>(
            SearchQueryBuilder.TAG,
            query,
            1,
            0
        )

        return SearchBundle().apply {
            this.appList =
                AppDetailsHelper(authData).getAppByPackageName(packageNames).toMutableList()
            this.query = query
            this.subBundles = hashSetOf()
        }
    }

    @Throws(Exception::class)
    override fun searchSuggestions(query: String): List<SearchSuggestEntry> {
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
