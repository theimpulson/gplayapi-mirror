/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.editor

class EditorChoiceBundle {
    var id: Int = -1
    var bundleTitle: String = String()
    var bundleChoiceClusters: List<EditorChoiceCluster> = ArrayList()
}
