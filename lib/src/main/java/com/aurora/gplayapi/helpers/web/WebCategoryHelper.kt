package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.rpc.CategoryBuilder
import com.aurora.gplayapi.data.models.Category
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.helpers.contracts.CategoryContract
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig

class WebCategoryHelper : BaseWebHelper(), CategoryContract {
    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    override fun getAllCategoriesList(type: Category.Type): List<Category> {
        val webType: Category.WebType = when (type) {
            Category.Type.GAME -> Category.WebType.GAME
            Category.Type.APPLICATION -> Category.WebType.APPLICATION
            Category.Type.FAMILY -> Category.WebType.FAMILY
        }

        return getAllCategoriesList(webType)
    }

    override fun getSubCategoryBundle(homeUrl: String): StreamBundle {
        return StreamBundle()
    }

    private fun getAllCategoriesList(type: Category.WebType): List<Category> {
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

    private fun parseCategory(type: Category.WebType, payload: Collection<Any>): List<Category> {
        return (payload.dig<ArrayList<Any>>(type.value, 3) ?: arrayListOf()).map {
            Category().apply {
                title = it.dig(1, 1) ?: ""
                browseUrl = it.dig(1, 0) ?: ""
                imageUrl = getImageUrl(type, browseUrl)
            }
        }
    }

    private fun getImageUrl(type: Category.WebType, browseUrl: String): String {
        // Sample: https://play-apps-features.googleusercontent.com/png/gm3_categories_icons_apps/auto_and_vehicles.png
        val category = when (type) {
            Category.WebType.APPLICATION -> "apps"
            Category.WebType.GAME -> "games"
            Category.WebType.FAMILY -> "family"
        }

        var subCategory = browseUrl
            .split("/")
            .last()
            .lowercase()
            .replace("game_", "")
            .replace("app_", "")


        subCategory = when (subCategory) {
            "watch_face" -> return "watch_faces"
            "watch_app" -> return "watch_apps"
            else -> subCategory
        }

        return "https://play-apps-features.googleusercontent.com/png/gm3_categories_icons_${category}/${subCategory}.png"
    }
}
