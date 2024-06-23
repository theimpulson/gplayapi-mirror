package com.aurora.gplayapi.helpers.contracts

import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.data.models.SearchBundle

interface SearchContract {
    fun searchSuggestions(query: String): List<SearchSuggestEntry>

    fun searchResults(query: String, nextPageUrl: String = ""): SearchBundle

    fun next(bundleSet: MutableSet<SearchBundle.SubBundle>): SearchBundle
}