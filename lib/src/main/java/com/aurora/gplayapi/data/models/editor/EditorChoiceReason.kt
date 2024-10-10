/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.editor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorChoiceReason(var bulletins: List<String>, var description: String) : Parcelable
