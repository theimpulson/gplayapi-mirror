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

package com.aurora.gplayapi

object Constants {

    const val IMAGE_TYPE_APP_SCREENSHOT = 1
    const val IMAGE_TYPE_PLAY_STORE_PAGE_BACKGROUND = 2
    const val IMAGE_TYPE_YOUTUBE_VIDEO_LINK = 3
    const val IMAGE_TYPE_APP_ICON = 4
    const val IMAGE_TYPE_CATEGORY_ICON = 5
    const val IMAGE_TYPE_YOUTUBE_VIDEO_THUMBNAIL = 13
    const val IMAGE_TYPE_GOOGLE_PLUS_BACKGROUND = 15

    enum class ABUSE(var value: Int) {
        SEXUAL_CONTENT(1),
        GRAPHIC_VIOLENCE(3),
        HATEFUL_OR_ABUSIVE_CONTENT(4),
        IMPROPER_CONTENT_RATING(5),
        HARMFUL_TO_DEVICE_OR_DATA(7),
        OTHER(8), ILLEGAL_PRESCRIPTION(11),
        IMPERSONATION(12);
    }

    enum class PATCH_FORMAT(var value: Int) {
        GDIFF(1),
        GZIPPED_GDIFF(2),
        GZIPPED_BSDIFF(3),
        UNKNOWN_4(4),
        UNKNOWN_5(5);
    }

    enum class Restriction(val restriction: Int) {
        GENERIC(-1),
        NOT_RESTRICTED(1),
        GEO_RESTRICTED(2), // This item isn't available in your country.
        DEVICE_RESTRICTED(7),
        NOT_IN_GROUP(8), // You're not in the targeted group for this item.
        UNKNOWN(9),
        CARRIER_RESTRICTED(10), // This item isn't available on your carrier.
        COUNTRY_OR_CARRIER_RESTRICTED(11), // This item isn't available in your country or on your carrier.
        PARENTAL_CONTROL_RESTRICTION(12), // Parental controls restrict downloading of this item.
        ADMIN_RESTRICTED(21), // Your administrator has not given you access to this item.
        ADMIN_PERMISSION_NOT_ACCEPTED(22), // Your administrator has not accepted permissions for this item.
        AGE_RESTRICTED(30), // guess: Google user needs to be of full age
        APP_OUTDATED(32); // guess: App needs to be updated -> use old device profile for download

        companion object {
            fun forInt(restriction: Int): Restriction {
                return when (restriction) {
                    1 -> NOT_RESTRICTED
                    2 -> GEO_RESTRICTED
                    7 -> DEVICE_RESTRICTED
                    8 -> NOT_IN_GROUP
                    9 -> UNKNOWN
                    10 -> CARRIER_RESTRICTED
                    11 -> COUNTRY_OR_CARRIER_RESTRICTED
                    12 -> PARENTAL_CONTROL_RESTRICTION
                    21 -> ADMIN_RESTRICTED
                    22 -> ADMIN_PERMISSION_NOT_ACCEPTED
                    30 -> AGE_RESTRICTED
                    32 -> APP_OUTDATED
                    else -> GENERIC
                }
            }
        }
    }
}
