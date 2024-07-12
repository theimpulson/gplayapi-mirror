package com.aurora.gplayapi.utils

import com.aurora.gplayapi.helpers.contracts.StreamContract

object CategoryUtil {
    fun getCategoryFromUrl(url: String): StreamContract.Category {
        val rawCategory = if (url.contains("cat=")) {
            extractNativeCategory(url) ?: ""
        } else {
            extractWebCategory(url)
        }

        return StreamContract.Category.NONE.apply {
            value = rawCategory
        }
    }

    private fun extractNativeCategory(url: String): String? {
        val regex = Regex("cat=([^&]*)")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)
    }

    private fun extractWebCategory(url: String): String {
        return url.split("/").last()
    }
}