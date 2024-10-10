/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.sale

data class SaleBundle(
    val currency: String,
    val currencycode: String,
    val sales: List<AppOnSale>
)
