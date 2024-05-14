/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

class Category {
    var title: String = String()
    var imageUrl: String = String()
    var browseUrl: String = String()
    var color: String = String()
    var type: Type = Type.APPLICATION

    enum class Type(val value: String) {
        GAME("GAME"),
        APPLICATION("APPLICATION");
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Category -> title == other.title
            else -> false
        }
    }
}
