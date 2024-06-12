package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.Constants
import com.aurora.gplayapi.data.builders.rpc.CategoryBuilder
import com.aurora.gplayapi.data.models.Category
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig

class WebCategoryHelper : BaseWebHelper() {
    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    fun fetch(type: Constants.WebCategoryType): List<Category> {
        val response = execute(arrayOf(CategoryBuilder.build()))
        val payload = response.dig<Collection<Any>>(
            CategoryBuilder.TAG,
            CategoryBuilder.TAG,
        )

        if (payload.isNullOrEmpty()) {
            return listOf()
        }

        val categoryPayload = payload.dig<ArrayList<Any>>(0, 1, 0, 3) ?: arrayListOf()

        return parseCategory(type, categoryPayload)
    }

    private fun parseCategory(
        type: Constants.WebCategoryType,
        payload: Collection<Any>
    ): List<Category> {
        return (payload.dig<ArrayList<Any>>(type.value, 3) ?: arrayListOf()).map {
            Category().apply {
                title = it.dig(1, 1) ?: ""
                browseUrl = it.dig(1, 0) ?: ""
            }
        }
    }
}
