/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

data class ReviewCluster(
    val id: Int = -1,
    val nextPageUrl: String = String(),
    val reviewList: List<Review> = listOf()
) {

    fun hasNext(): Boolean {
        return nextPageUrl.isNotBlank()
    }
}
