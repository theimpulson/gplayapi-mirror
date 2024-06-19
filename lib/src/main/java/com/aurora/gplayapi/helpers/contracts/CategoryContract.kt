package com.aurora.gplayapi.helpers.contracts

import com.aurora.gplayapi.data.models.Category
import com.aurora.gplayapi.data.models.StreamBundle

interface CategoryContract {
    fun getAllCategoriesList(type: Category.Type): List<Category>

    fun getSubCategoryBundle(homeUrl: String): StreamBundle
}