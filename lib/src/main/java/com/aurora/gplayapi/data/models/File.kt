/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
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
