/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

class ReviewCluster {
    var id: Int = -1
    var nextPageUrl: String = String()
    var reviewList: MutableList<Review> = mutableListOf()

    fun hasNext(): Boolean {
        return nextPageUrl.isNotBlank()
    }
}
