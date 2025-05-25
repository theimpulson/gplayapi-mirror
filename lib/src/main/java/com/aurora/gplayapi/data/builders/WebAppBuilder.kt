/*
 * SPDX-FileCopyrightText: 2023-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders

import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.Artwork
import com.aurora.gplayapi.data.models.ContentRating
import com.aurora.gplayapi.data.models.Rating
import com.aurora.gplayapi.utils.dig

internal object WebAppBuilder {

    fun build(packageName: String, payload: Any): App {
        val appInfoPayload = payload.dig<ArrayList<Any>>(1, 2) ?: arrayListOf()
        val downloadsPayload = appInfoPayload.dig<ArrayList<Any>>(13) ?: arrayListOf()
        val offersPayload = appInfoPayload.dig<ArrayList<Any>>(57, 0, 0, 0, 0) ?: arrayListOf()
        val ratingPayload = appInfoPayload.dig<ArrayList<Any>>(51) ?: arrayListOf()
        val contentRatingPayload = appInfoPayload.dig<ArrayList<Any>>(9) ?: arrayListOf()

        return App(
            packageName = packageName,
            id = packageName.hashCode(),
            categoryId = 3,
            displayName = appInfoPayload.dig<String>(0, 0) ?: "",
            description = appInfoPayload.dig<String>(72, 0, 1) ?: "",
            shortDescription = appInfoPayload.dig<String>(73, 0, 1) ?: "",
            changes = appInfoPayload.dig<String>(144, 1, 1) ?: "",
            versionName = appInfoPayload.dig<String>(140, 0, 0, 0) ?: "",

            iconArtwork = parseArtwork(
                appInfoPayload.dig<ArrayList<Any>>(95, 0) ?: arrayListOf(),
                4
            ),
            coverArtwork = parseArtwork(
                appInfoPayload.dig<ArrayList<Any>>(96, 0) ?: arrayListOf(),
                2
            ),
            videoArtwork = parseArtwork(
                appInfoPayload.dig<ArrayList<Any>>(100, 1, 0) ?: arrayListOf(), 13
            ),
            screenshots = (appInfoPayload.dig<ArrayList<ArrayList<Any>>>(78, 0)
                ?: arrayListOf()).map { parseArtwork(it, 1) }.toMutableList(),

            categoryName = appInfoPayload.dig<String>(79, 0, 0, 2) ?: "",
            targetSdk = (appInfoPayload.dig<Double>(140, 1, 0, 0, 0) ?: 0).toInt(),
            updatedOn = appInfoPayload.dig<String>(145, 0, 0) ?: "",

            containsAds = (appInfoPayload.dig<String>(48, 0) ?: "").isNotBlank(),
            earlyAccess = (appInfoPayload.dig<String>(18, 2) ?: "").isNotBlank(),
            inPlayStore = true,
            developerName = appInfoPayload.dig<String>(68, 0) ?: "",
            developerEmail = appInfoPayload.dig<String>(69, 1, 0) ?: "",
            developerWebsite = appInfoPayload.dig<String>(69, 0, 5, 2) ?: "",
            developerAddress = appInfoPayload.dig<String>(69, 2, 0) ?: "",

            downloadString = appInfoPayload.dig<String>(13, 3) ?: "",
            installs = (downloadsPayload.dig<Double>(2) ?: 0.0).toLong(),
            isFree = (offersPayload.dig<Double>(1, 0, 0) ?: 0.0) == 0.0,
            price = offersPayload.dig<String>(1, 0, 2) ?: "",
            rating = parseRating(ratingPayload) ?: Rating(),
            labeledRating = ratingPayload.dig<String>(0, 0) ?: "",
            contentRating = ContentRating(
                title = contentRatingPayload.dig<String>(0) ?: "",
                description = contentRatingPayload.dig<String>(2, 1) ?: "",
                recommendation = contentRatingPayload.dig<String>(3 , 1) ?: "",
                artwork = parseArtwork(
                    contentRatingPayload.dig<ArrayList<Any>>(1) ?: arrayListOf(),
                    3
                ),
                recommendationAndDescriptionHtml = contentRatingPayload.dig<String>(4, 1) ?: ""
            )
        )
    }

    private fun parseRating(payload: ArrayList<Any>): Rating? {
        if (payload.isEmpty()) return null
        return Rating(
            average = (payload.dig<Double>(0, 1) ?: 0.0).toFloat(),
            oneStar = (payload.dig<Long>(1, 1, 1, 1) ?: 0L),
            twoStar = (payload.dig<Long>(1, 2, 1, 1) ?: 0L),
            threeStar = (payload.dig<Long>(1, 3, 1, 1) ?: 0L),
            fourStar = (payload.dig<Long>(1, 4, 1, 1) ?: 0L),
            fiveStar = (payload.dig<Long>(1, 5, 1, 1) ?: 0L),
            label = payload.dig<String>(2, 0) ?: "",
            abbreviatedLabel = payload.dig<String>(0, 0) ?: ""
        )
    }

    fun parseArtwork(payload: ArrayList<Any>, artworkType: Int = 0): Artwork {
        return Artwork(
            type = artworkType,
            url = payload.dig<String>(3, 2) ?: "",
            height = (payload.dig<Double>(2, 0) ?: 0.0).toInt(),
            width = (payload.dig<Double>(2, 1) ?: 0.0).toInt()
        )
    }
}
