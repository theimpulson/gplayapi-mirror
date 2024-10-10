/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.sale

data class AppOnSale(
    val category: String,
    val rating: String,
    val id: String,
    val idandroid: String,
    val downloadsmin: String,
    val nameapp: String,
    val price: String,
    val icon: String,
    val oldprice: String,
    val dateup: String
)
