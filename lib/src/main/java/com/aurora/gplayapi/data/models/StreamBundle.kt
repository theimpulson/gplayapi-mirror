/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

class StreamBundle {
    val id: Int = -1
    var streamTitle: String = String()
    var streamNextPageUrl: String = String()
    var streamClusters: MutableMap<Int, StreamCluster> = mutableMapOf()

    operator fun hasNext(): Boolean {
        return streamNextPageUrl.isNotBlank()
    }

    fun hasCluster(): Boolean {
        return streamClusters.isNotEmpty()
    }
}
