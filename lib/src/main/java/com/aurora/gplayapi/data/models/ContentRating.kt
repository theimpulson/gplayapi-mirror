/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentRating(
    val title: String = String(),
    val description: String = String(),
    val recommendation: String = String(),
    val artwork: Artwork = Artwork(),
    val recommendationAndDescriptionHtml: String = String()
) : Parcelable
