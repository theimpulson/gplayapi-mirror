/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.BulkDetailsRequest
import com.aurora.gplayapi.DetailsResponse
import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.ListResponse
import com.aurora.gplayapi.Payload
import com.aurora.gplayapi.TestingProgramRequest
import com.aurora.gplayapi.data.builders.AppBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.details.DevStream
import com.aurora.gplayapi.data.models.details.TestingProgramStatus
import com.aurora.gplayapi.data.providers.HeaderProvider.getDefaultHeaders
import com.aurora.gplayapi.exceptions.InternalException
import com.aurora.gplayapi.helpers.contracts.AppDetailsContract
import com.aurora.gplayapi.network.IHttpClient
import java.io.IOException

class AppDetailsHelper(authData: AuthData) : NativeHelper(authData), AppDetailsContract {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    private fun getDevStream(payload: Payload): DevStream {
        val listResponse: ListResponse = payload.listResponse

        with(listResponse) {
            if (hasItem()) {
                with(item) {
                    if (hasAnnotations() && annotations.hasOverlayMetaData()) {
                        with(annotations.overlayMetaData) {
                            return DevStream(
                                title = overlayTitle?.title.orEmpty(),
                                imgUrl = overlayTitle?.compositeImage?.url.orEmpty(),
                                description = overlayDescription?.description.orEmpty(),
                                streamBundle = getStreamBundle(payload.listResponse)
                            )
                        }
                    }
                }
            }
        }

        return DevStream.EMPTY
    }

    @Throws(Exception::class)
    fun getDetailsResponseByPackageName(packageName: String): DetailsResponse {
        val headers: Map<String, String> = getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()
        params["doc"] = packageName

        val playResponse = httpClient.get(GooglePlayApi.URL_DETAILS, headers, params)
        if (playResponse.isSuccessful) {
            return getResponseFromBytes(playResponse.responseBytes)
        } else {
            throw InternalException.AppNotFound(playResponse.errorString)
        }
    }

    @Throws(Exception::class)
    override fun getAppByPackageName(packageName: String): App {
        val detailsResponse = getDetailsResponseByPackageName(packageName)
        return AppBuilder.build(detailsResponse)
    }

    @Throws(Exception::class)
    override fun getAppByPackageName(packageNames: List<String>): List<App> {
        if (packageNames.isEmpty()) {
            return emptyList()
        }

        val appList: MutableList<App> = ArrayList()
        val headers: MutableMap<String, String> = getDefaultHeaders(authData)
        val request = BulkDetailsRequest.newBuilder()
            .addAllDocId(packageNames)
            .build()
            .toByteArray()

        if (!headers.containsKey("Content-Type")) {
            headers["Content-Type"] = "application/x-protobuf"
        }

        val playResponse = httpClient.post(GooglePlayApi.URL_BULK_DETAILS, headers, request)

        if (playResponse.isSuccessful) {
            val payload = getPayLoadFromBytes(playResponse.responseBytes)
            if (payload.hasBulkDetailsResponse()) {
                val bulkDetailsResponse = payload.bulkDetailsResponse
                for (entry in bulkDetailsResponse.entryList) {
                    val app = AppBuilder.build(entry.item)
                    // System.out.printf("%s -> %s\n", app.displayName, app.packageName);
                    appList.add(app)
                }
            }
            return appList
        } else {
            throw InternalException.Server(playResponse.code, playResponse.errorString)
        }
    }

    // TODO: Move to contract
    @Throws(Exception::class)
    fun getDetailsStream(streamUrl: String): StreamBundle {
        val headers: Map<String, String> = getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()

        val playResponse = httpClient.get(
            "${GooglePlayApi.URL_FDFE}/$streamUrl",
            headers,
            params
        )

        if (playResponse.isSuccessful) {
            val payload = getPayLoadFromBytes(playResponse.responseBytes)
            return getStreamBundle(payload.listResponse)
        }

        return StreamBundle()
    }

    fun getDeveloperStream(devId: String): DevStream {
        val headers: Map<String, String> = getDefaultHeaders(authData)
        val params: MutableMap<String, String> = HashMap()

        val playResponse = httpClient.get(
            "${GooglePlayApi.URL_FDFE}/getDeveloperPageStream?docid=developer-$devId",
            headers,
            params
        )

        if (playResponse.isSuccessful) {
            val payload = getPayLoadFromBytes(playResponse.responseBytes)
            return getDevStream(payload)
        }

        return DevStream()
    }

    @Throws(IOException::class)
    fun testingProgram(packageName: String?, subscribe: Boolean = true): TestingProgramStatus {
        val request = TestingProgramRequest.newBuilder()
            .setPackageName(packageName)
            .setSubscribe(subscribe)
            .build()

        val playResponse = httpClient.post(
            GooglePlayApi.URL_TESTING_PROGRAM,
            getDefaultHeaders(authData),
            request.toByteArray()
        )

        return if (playResponse.isSuccessful) {
            val payload = getPayLoadFromBytes(playResponse.responseBytes)
            if (payload.hasTestingProgramResponse() &&
                payload.testingProgramResponse.hasResult() &&
                payload.testingProgramResponse.result.hasDetails()
            ) {
                val details = payload.testingProgramResponse.result.details
                return TestingProgramStatus(
                    subscribed = details.hasSubscribed() && details.subscribed,
                    unsubscribed = details.hasUnsubscribed() && details.unsubscribed
                )
            } else {
                TestingProgramStatus()
            }
        } else {
            TestingProgramStatus()
        }
    }
}
