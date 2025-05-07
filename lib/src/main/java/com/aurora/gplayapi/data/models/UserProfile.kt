/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val name: String = String(),
    val email: String = String(),
    val artwork: Artwork = Artwork()
)
