/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.editor

import com.aurora.gplayapi.data.models.Artwork

class EditorChoiceCluster {
    var id: Int = -1
    var clusterTitle: String = String()
    var clusterBrowseUrl: String = String()
    var clusterArtwork: List<Artwork> = ArrayList()
}
