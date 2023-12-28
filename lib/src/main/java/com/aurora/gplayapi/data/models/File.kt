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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class File(
    var id: String = UUID.randomUUID().toString(),
    var name: String = String(),
    var url: String = String(),
    var size: Long = 0L,
    var type: FileType = FileType.BASE,
    var sha1: String = String(),
    var sha256: String = String()
) : Parcelable {

    enum class FileType {
        BASE, OBB, PATCH, SPLIT
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is File -> other.id == id
            else -> false
        }
    }
}
