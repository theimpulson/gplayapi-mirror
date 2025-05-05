/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.editor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorChoiceReason(
    val bulletins: List<String>,
    val description: String
) : Parcelable
