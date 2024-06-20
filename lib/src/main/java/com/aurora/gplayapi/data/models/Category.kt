/*
 *     GPlayApi
 *     Copyright (C) 2020  Aurora OSS
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package com.aurora.gplayapi.data.models

data class Category(
    val title: String = String(),
    val imageUrl: String = String(),
    val browseUrl: String = String(),
    val color: String = String(),
    val type: Type = Type.APPLICATION
) {

    enum class Type(val value: String) {
        APPLICATION("APPLICATION"),
        GAME("GAME"),
        FAMILY("FAMILY"),
    }

    enum class WebType(val value: Int) {
        APPLICATION(0),
        GAME(1),
        FAMILY(2),
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
