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

package com.aurora.gplayapi.data.builders

import com.aurora.gplayapi.*
import com.aurora.gplayapi.data.builders.ReviewBuilder.build
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.details.Badge
import com.aurora.gplayapi.data.models.File
import com.aurora.gplayapi.data.models.Rating

object AppBuilder {

    fun build(detailsResponse: DetailsResponse): App {
        val app = build(detailsResponse.item)
        app.footerHtml = if (detailsResponse.footerHtml.isNotBlank()) detailsResponse.footerHtml else String()
        app.features = detailsResponse.features

        if (detailsResponse.hasUserReview()) {
            app.userReview = build(detailsResponse.userReview)
        }

        return app
    }

    fun build(item: Item): App {
        val appDetails = item.details.appDetails
        val app = App(appDetails.packageName)
        app.id = appDetails.packageName.hashCode()
        app.displayName = item.title
        app.description = item.descriptionHtml
        app.shortDescription = item.promotionalDescription
        app.categoryId = item.annotations.categoryInfo.appCategory
        app.restriction = Constants.Restriction.forInt(item.availability.restriction)

        if (item.offerCount > 0) {
            app.offerType = item.getOffer(0).offerType
            app.isFree = item.getOffer(0).micros == 0L
            app.price = item.getOffer(0).formattedAmount
        }

        app.packageName = appDetails.packageName
        app.versionName = appDetails.versionString
        app.versionCode = appDetails.versionCode
        app.categoryName = appDetails.categoryName
        app.size = appDetails.installationSize
        app.installs = appDetails.downloadCount
        app.downloadString = appDetails.downloadLabelAbbreviated
        app.updated = appDetails.uploadDate
        app.changes = appDetails.recentChangesHtml
        app.permissions = appDetails.permissionList
        app.containsAds = appDetails.hasInstallNotes()
        app.inPlayStore = true
        app.earlyAccess = appDetails.hasEarlyAccessInfo()
        app.testingProgramAvailable = appDetails.hasTestingProgramInfo()
        app.labeledRating = item.aggregateRating.ratingLabel
        app.developerName = appDetails.developerName
        app.developerEmail = appDetails.developerEmail
        app.developerAddress = appDetails.developerAddress
        app.developerWebsite = appDetails.developerWebsite

        if (app.developerName.isNotEmpty())
            app.developerName = item.creator

        if (appDetails.hasInstantLink && appDetails.instantLink!!.isNotEmpty()) {
            app.instantAppLink = appDetails.instantLink
        }

        if (app.testingProgramAvailable) {
            app.testingProgramOptedIn = appDetails.testingProgramInfo.subscribed
            app.testingProgramEmail = appDetails.testingProgramInfo.testingProgramEmail
        }

        parseAppInfo(app, item)
        parseDisplayBadges(app,item)
        parseInfoBadges(app,item)
        parseAppRating(app, item.aggregateRating)
        parseArtwork(app, item.imageList)
        parseDependencies(app, appDetails)
        parseFiles(app, appDetails)
        return app
    }

    private fun parseDisplayBadges(app: App, item: Item) {
        item.annotations?.let { annotations ->
            annotations.displayBadgeList?.let { badges ->
                badges.forEach {
                    app.displayBadges.add(
                            Badge().apply {
                                textMajor = it.major
                                textMinor = it.minor
                                textMinorHtml = it.minorHtml
                                textDescription = it.description
                                artwork = DetailsBuilder.build(it.image)
                                link = it.link.toString()
                            }
                    )
                }
            }
        }
    }

    private fun parseInfoBadges(app: App, item: Item) {
        item.annotations?.let { annotations ->
            annotations.infoBadgeList?.let { badges ->
                badges.forEach {
                    app.infoBadges.add(DetailsBuilder.build(it))
                }
            }

            annotations.badgeForLegacyRating?.let {
                app.infoBadges.add(DetailsBuilder.build(it))
            }
        }
    }

    private fun parseFiles(app: App, appDetails: AppDetails) {
        app.fileList = appDetails.fileList
                .map {
                    var fileType = File.FileType.BASE
                    var fileName = "${app.packageName}.${app.versionCode}.apk"

                    if (it.hasSplitId()) {
                        fileType = File.FileType.SPLIT
                        fileName = "${it.splitId}.${app.versionCode}.apk"
                    } else {
                        if (it.fileType == 1) {
                            fileName = "${app.packageName}.${app.versionCode}.obb"
                            fileType = File.FileType.OBB
                        }
                    }
                    File().apply {
                        name = fileName
                        type = fileType
                        size = it.size
                    }
                }
                .toMutableList()
    }

    private fun parseAppRating(app: App, aggregateRating: AggregateRating) {
        val rating = Rating(
                aggregateRating.starRating,
                aggregateRating.oneStarRatings,
                aggregateRating.twoStarRatings,
                aggregateRating.threeStarRatings,
                aggregateRating.fourStarRatings,
                aggregateRating.fiveStarRatings,
                aggregateRating.thumbsUpCount,
                aggregateRating.thumbsDownCount,
                aggregateRating.ratingLabel,
                aggregateRating.ratingCountLabelAbbreviated
        )
        app.rating = rating
    }

    private fun parseDependencies(app: App, appDetails: AppDetails) {
        if (appDetails.hasDependencies()) {
            appDetails.dependencies.let {
                app.dependencies.apply {
                    it.dependencyList.forEach { dependency ->
                        dependentPackages.add(dependency.packageName)
                    }

                    it.splitApksList.forEach { splitId ->
                        dependentSplits.add(splitId)
                    }

                    totalSize = it.size
                    targetSDK = it.targetSdk
                }
            }
        }
    }

    private fun parseAppInfo(app: App, item: Item) {
        if (item.hasAppInfo()) {
            app.appInfo.apply {
                item.appInfo.sectionList.forEach {
                    if (it.hasLabel() && it.hasContainer() && it.container.hasDescription()) {
                        appInfoMap[it.label] = it.container.description
                    }
                }
            }
        }
    }

    private fun parseArtwork(app: App, images: List<Image>) {
        for (image in images) {
            val artwork = DetailsBuilder.build(image)
            when (image.imageType) {
                Constants.IMAGE_TYPE_CATEGORY_ICON -> app.categoryArtwork = artwork
                Constants.IMAGE_TYPE_APP_ICON -> app.iconArtwork = artwork
                Constants.IMAGE_TYPE_YOUTUBE_VIDEO_LINK -> app.videoArtwork = artwork
                Constants.IMAGE_TYPE_PLAY_STORE_PAGE_BACKGROUND -> app.coverArtwork = artwork
                Constants.IMAGE_TYPE_APP_SCREENSHOT -> app.screenshots.add(artwork)
            }
        }
    }
}