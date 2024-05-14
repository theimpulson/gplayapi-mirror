/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders.rpc

object SearchSuggestionQueryBuilder {
    public val TAG = "SSQB"
    fun build(query: String, tag: String = TAG): String {
        return """
            ["teXCtc","[null,[\"$query\"],[10],[2,1],4]",null,"$tag@$query"]
        """
            .trimStart()
            .trimEnd()
            .trimIndent()
    }
}
