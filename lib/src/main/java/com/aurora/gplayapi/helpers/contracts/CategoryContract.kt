package com.aurora.gplayapi.helpers.contracts

import com.aurora.gplayapi.data.models.Category

interface CategoryContract {
    fun getAllCategories(type: Category.Type): List<Category>
}