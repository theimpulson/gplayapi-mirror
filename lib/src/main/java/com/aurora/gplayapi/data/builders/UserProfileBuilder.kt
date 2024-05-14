/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.builders

import com.aurora.gplayapi.data.models.UserProfile

object UserProfileBuilder {

    fun build(userProfileProto: com.aurora.gplayapi.UserProfile): UserProfile {
        return UserProfile().apply {
            name = userProfileProto.name
            email = userProfileProto.profileDescription
            artwork = ArtworkBuilder.build(userProfileProto.getImage(0))
        }
    }
}
