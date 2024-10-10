/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders

import com.aurora.gplayapi.Constants
import com.aurora.gplayapi.data.models.Review

internal object ReviewBuilder {

    fun build(reviewProto: com.aurora.gplayapi.Review): Review {
        return Review().apply {
            comment = reviewProto.comment
            commentId = reviewProto.commentId
            title = reviewProto.title
            rating = reviewProto.starRating
            userName = reviewProto.userProfile.name
            timeStamp = reviewProto.timestamp
            appVersion = reviewProto.version

            reviewProto.userProfile.imageList?.forEach {
                if (it.imageType == Constants.IMAGE_TYPE_APP_ICON) {
                    userPhotoUrl = it.imageUrl
                }
            }
        }
    }
}
