/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

class StreamCluster {
    var id: Int = -1
    var clusterTitle: String = String()
    var clusterSubtitle: String = String()
    var clusterNextPageUrl: String = String()
    var clusterBrowseUrl: String = String()
    var clusterAppList: MutableList<App> = mutableListOf()

    fun hasNext(): Boolean {
        return clusterNextPageUrl.isNotBlank()
    }
}
