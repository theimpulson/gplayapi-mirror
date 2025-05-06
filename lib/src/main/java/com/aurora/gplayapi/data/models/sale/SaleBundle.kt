/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.sale

import kotlinx.serialization.Serializable

@Serializable
data class SaleBundle(
    val currency: String,
    val currencycode: String,
    val sales: List<AppOnSale>
)
