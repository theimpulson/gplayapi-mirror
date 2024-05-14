/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models.details

import com.aurora.gplayapi.data.models.StreamBundle

class DevStream {
    var title: String = String()
    var description: String = String()
    var imgUrl: String = String()
    var streamBundle: StreamBundle = StreamBundle()
}
