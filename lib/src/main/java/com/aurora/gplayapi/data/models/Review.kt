/*
 * SPDX-FileCopyrightText: 2020-2021 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    var title: String = String(),
    var comment: String = String(),
    var commentId: String = String(),
    var userName: String = String(),
    var userPhotoUrl: String = String(),
    var appVersion: String = String(),
    var rating: Int = 0,
    var timeStamp: Long = 0L
) : Parcelable {

    override fun hashCode(): Int {
        return commentId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Review -> other.commentId == commentId
            else -> false
        }
    }

    enum class Filter(val value: String) {
        ALL("ALL"),
        POSITIVE("1"),
        CRITICAL("2"),
        FIVE("5"),
        FOUR("4"),
        THREE("3"),
        TWO("2"),
        ONE("1");
    }
}
