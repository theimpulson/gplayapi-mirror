/*
 * SPDX-FileCopyrightText: 2020-2023 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2024 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.data.models

import android.os.Parcelable
import com.aurora.gplayapi.Constants.Restriction
import com.aurora.gplayapi.data.models.details.AppInfo
import com.aurora.gplayapi.data.models.details.Badge
import com.aurora.gplayapi.data.models.details.Chip
import com.aurora.gplayapi.data.models.details.Dependencies
import com.aurora.gplayapi.data.models.details.TestingProgram
import com.aurora.gplayapi.data.models.editor.EditorChoiceReason
import kotlinx.parcelize.Parcelize

@Parcelize
data class App(
    var packageName: String,
    var id: Int = 0,
    var appInfo: AppInfo = AppInfo(),
    var categoryArtwork: Artwork = Artwork(),
    var categoryId: Int = 0,
    var categoryName: String = String(),
    var categoryStreamUrl: String? = String(),
    var changes: String = String(),
    var chips: MutableList<Chip> = mutableListOf(),
    var containsAds: Boolean = false,
    var coverArtwork: Artwork = Artwork(),
    var dependencies: Dependencies = Dependencies(),
    var description: String = String(),
    var detailsStreamUrl: String? = String(),
    var detailsPostAcquireStreamUrl: String? = String(),
    var developerAddress: String = String(),
    var developerEmail: String = String(),
    var developerName: String = String(),
    var developerWebsite: String = String(),
    var displayBadges: MutableList<Badge> = mutableListOf(),
    var displayName: String = String(),
    var editorReason: EditorChoiceReason? = null,
    var downloadString: String = String(),
    var earlyAccess: Boolean = false,
    var fileList: MutableList<File> = mutableListOf(),
    var footerHtml: String = String(),
    var iconArtwork: Artwork = Artwork(),
    var infoBadges: MutableList<Badge> = mutableListOf(),
    var inPlayStore: Boolean = false,
    var installs: Long = 0,
    var instantAppLink: String = String(),
    var isFree: Boolean = false,
    var isInstalled: Boolean = false,
    var isSystem: Boolean = false,
    var labeledRating: String = String(),
    var liveStreamUrl: String? = String(),
    var offerDetails: MutableMap<String, String> = mutableMapOf(),
    var offerType: Int = 0,
    var permissions: MutableList<String> = mutableListOf(),
    var price: String = String(),
    var promotionStreamUrl: String? = String(),
    var rating: Rating = Rating(),
    var relatedLinks: MutableMap<String, String> = mutableMapOf(),
    var restriction: Restriction = Restriction.NOT_RESTRICTED,
    var screenshots: MutableList<Artwork> = mutableListOf(),
    var shareUrl: String = String(),
    var shortDescription: String = String(),
    var size: Long = 0,
    var targetSdk: Int = 21,
    var testingProgram: TestingProgram? = null,
    var userReview: Review = Review(),
    var updatedOn: String = String(),
    var versionCode: Long = 0,
    var versionName: String = String(),
    var videoArtwork: Artwork = Artwork(),
    var certificateHashList: MutableList<String> = mutableListOf(),
    var certificateSetList: MutableList<EncodedCertificateSet> = mutableListOf(),
    val compatibility: MutableList<ActiveDevice> = mutableListOf(),
    var contentRating: ContentRating = ContentRating(),
    var privacyPolicyUrl: String = String()
) : Parcelable {

    override fun hashCode(): Int {
        return packageName.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is App -> packageName == other.packageName
            else -> false
        }
    }
}
