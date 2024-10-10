/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.details

import com.aurora.gplayapi.data.models.StreamBundle

data class DevStream(
    val title: String = String(),
    val description: String = String(),
    val imgUrl: String = String(),
    val streamBundle: StreamBundle = StreamBundle()
)
