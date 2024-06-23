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

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.Constants.PATCH_FORMAT
import com.aurora.gplayapi.DeliveryResponse
import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.ResponseWrapper
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.File
import com.aurora.gplayapi.data.providers.HeaderProvider
import com.aurora.gplayapi.exceptions.InternalException
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.CertUtil
import java.io.IOException

class PurchaseHelper(authData: AuthData) : NativeHelper(authData) {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    /**
     * @param offset
     * @param getAllAppDetails
     * <code>false</code>: the returned App instances will only contain basic properties
     * <code>true</code>: all App details are retrieved using AppDetailsHelper
     * @return list of apps purchased by the selected account, starting at offset.
     * The number of entries is defined by Google (usually less than 20)
     */
    fun getPurchaseHistory(offset: Int = 0, getAllAppDetails: Boolean = true): List<App> {
        val headers: MutableMap<String, String> = HeaderProvider.getDefaultHeaders(authData)
        val params: MutableMap<String, String> = mutableMapOf(
            "o" to "$offset"
        )
        val playResponse = httpClient.get(
            GooglePlayApi.PURCHASE_HISTORY_URL,
            headers,
            params
        )

        val purchaseAppList: MutableList<App> = mutableListOf()
        val listResponse: ListResponse = getResponseFromBytes(playResponse.responseBytes)
        if (listResponse.itemCount > 0) {
            for (item in listResponse.itemList) {
                for (subItem in item.subItemList) {
                    if (item.subItemCount > 0) {
                        if (item.hasAnnotations() &&
                            item.annotations.hasPurchaseHistoryDetails() &&
                            item.annotations.purchaseHistoryDetails.hasPurchaseStatus()
                        ) {
                            continue
                        }
                        purchaseAppList.addAll(getAppsFromItem(subItem))
                    }
                }
            }
        }
        if (!getAllAppDetails) {
            return purchaseAppList
        }
        return AppDetailsHelper(authData).getAppByPackageName(
            purchaseAppList.map { it.packageName }
                .distinct()
        )
    }

    @Throws(IOException::class)
    fun getDeliveryToken(packageName: String, versionCode: Int, offerType: Int, certificateHash: String): String {
        val params: MutableMap<String, String> = HashMap()
        params["ot"] = offerType.toString()
        params["doc"] = packageName
        params["vc"] = versionCode.toString()

        if (certificateHash.isNotEmpty()) {
            params["ch"] = certificateHash
        }

        val playResponse = httpClient.post(
            GooglePlayApi.PURCHASE_URL,
            HeaderProvider.getDefaultHeaders(authData),
            params
        )

        return if (playResponse.isSuccessful) {
            val payload = getPayLoadFromBytes(playResponse.responseBytes)
            payload.buyResponse.encodedDeliveryToken
        } else {
            ""
        }
    }

    @Throws(IOException::class)
    fun getDeliveryResponse(
        packageName: String,
        splitModule: String = String(),
        installedVersionCode: Int = 0,
        updateVersionCode: Int,
        offerType: Int,
        patchFormat: PATCH_FORMAT = PATCH_FORMAT.GDIFF,
        deliveryToken: String,
        certificateHash: String
    ): DeliveryResponse {
        val params: MutableMap<String, String> = HashMap()
        params["ot"] = offerType.toString()
        params["doc"] = packageName
        params["vc"] = updateVersionCode.toString()

        if (installedVersionCode > 0) {
            params["bvc"] = installedVersionCode.toString();
            params["pf"] = patchFormat.value.toString();
        }

        if (splitModule.isNotEmpty())  {
            params["mn"] = splitModule
        }

        if (certificateHash.isNotEmpty()) {
            params["ch"] = certificateHash
        }

        if (deliveryToken.isNotEmpty()) {
            params["dtok"] = deliveryToken
        }

        val playResponse =
            httpClient.get(
                GooglePlayApi.DELIVERY_URL,
                HeaderProvider.getDefaultHeaders(authData),
                params
            )
        val payload = ResponseWrapper.parseFrom(playResponse.responseBytes).payload
        return payload.deliveryResponse
    }

    @Throws(Exception::class)
    fun getOnDemandModule(
        packageName: String,
        splitModule: String,
        versionCode: Int,
        offerType: Int,
        certificateHash: String = ""
    ): File? {
        val deliveryToken = getDeliveryToken(packageName, versionCode, offerType, certificateHash)
        val deliveryResponse = getDeliveryResponse(
            packageName = packageName,
            splitModule = splitModule,
            updateVersionCode = versionCode,
            offerType = offerType,
            deliveryToken = deliveryToken,
            certificateHash = certificateHash
        )

        val modules = processDeliveryResponse(packageName, versionCode, deliveryResponse)
        return modules.find { it.name == "$splitModule.apk" }
    }

    @Throws(Exception::class)
    fun purchase(packageName: String, versionCode: Int, offerType: Int, certificateHash: String = ""): List<File> {
        val deliveryToken = getDeliveryToken(packageName, versionCode, offerType, certificateHash)
        val deliveryResponse = getDeliveryResponse(
            packageName = packageName,
            updateVersionCode = versionCode,
            offerType = offerType,
            deliveryToken = deliveryToken,
            certificateHash = certificateHash
        )

        return processDeliveryResponse(packageName, versionCode, deliveryResponse)
    }

    @Throws(Exception::class)
    private fun processDeliveryResponse(
        packageName: String,
        versionCode: Int,
        deliveryResponse: DeliveryResponse
    ): List<File> {
        when (deliveryResponse.status) {
            1 -> return getDownloadsFromDeliveryResponse(packageName, versionCode, deliveryResponse)
            2 -> throw InternalException.AppNotSupported()
            3 -> throw InternalException.AppNotPurchased()
            7 -> throw InternalException.AppRemoved()
            9 -> throw InternalException.AppNotSupported()
            else -> throw InternalException.Unknown()
        }
    }

    private fun getDownloadsFromDeliveryResponse(
        packageName: String?,
        versionCode: Int,
        deliveryResponse: DeliveryResponse?
    ): List<File> {
        val fileList: MutableList<File> = mutableListOf()
        if (deliveryResponse != null) {
            // Add base apk
            val androidAppDeliveryData = deliveryResponse.appDeliveryData
            if (androidAppDeliveryData != null) {
                fileList.add(
                    File().apply {
                        name = "base.apk"
                        url = androidAppDeliveryData.downloadUrl
                        size = androidAppDeliveryData.downloadSize
                        type = File.FileType.BASE
                        sha1 = CertUtil.decodeHash(androidAppDeliveryData.sha1)
                        sha256 = CertUtil.decodeHash(androidAppDeliveryData.sha256)
                    }
                )

                // Obb & patches (if any)
                val fileMetadataList = deliveryResponse.appDeliveryData.additionalFileList
                if (fileMetadataList != null) {
                    for (appFileMetadata in fileMetadataList) {
                        val isOBB = appFileMetadata.fileType == 0
                        val fileType = if (isOBB) "main" else "patch"
                        fileList.add(
                            File().apply {
                                name = "$fileType.$versionCode.$packageName.obb"
                                url = appFileMetadata.downloadUrl
                                size = appFileMetadata.size
                                type = if (isOBB) File.FileType.OBB else File.FileType.PATCH
                                sha1 = CertUtil.decodeHash(appFileMetadata.sha1)
                            }
                        )
                    }
                }

                // Add split apks (if any)
                val splitDeliveryDataList = deliveryResponse.appDeliveryData.splitDeliveryDataList
                if (fileMetadataList != null) {
                    for (splitDeliveryData in splitDeliveryDataList) {
                        fileList.add(
                            File().apply {
                                name = "${splitDeliveryData.name}.apk"
                                url = splitDeliveryData.downloadUrl
                                size = splitDeliveryData.downloadSize
                                type = File.FileType.SPLIT
                                sha1 = CertUtil.decodeHash(splitDeliveryData.sha1)
                                sha256 = CertUtil.decodeHash(splitDeliveryData.sha256)
                            }
                        )
                    }
                }
            }
        }

        if (fileList.isEmpty()) {
            throw InternalException.Unknown()
        }

        return fileList
    }
}
