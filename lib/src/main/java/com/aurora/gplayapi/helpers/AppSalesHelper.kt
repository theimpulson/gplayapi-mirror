/*
 * SPDX-FileCopyrightText: 2020-2024 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.GooglePlayApi
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.models.sale.SaleBundle
import com.aurora.gplayapi.network.IHttpClient
import kotlinx.serialization.json.Json
import java.io.IOException
import java.util.Locale

class AppSalesHelper(authData: AuthData) : NativeHelper(authData) {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @Throws(IOException::class)
    fun getAppsOnSale(page: Int, offer: Int = 100, type: String = ""): List<App> {
        val params: MutableMap<String, String> = mutableMapOf()
        params["page"] = page.toString()
        params["country"] = Locale.getDefault().country
        params["language"] = Locale.getDefault().language
        params["minreduc"] = offer.toString()

        /*params["typefilter"] = type Not sure of values */

        val playResponse = httpClient.get(GooglePlayApi.SALES_URL, headers = mapOf(), params)
        val saleBundle = Json.decodeFromString<SaleBundle>(String(playResponse.responseBytes))

        return if (saleBundle.sales.isEmpty()) {
            listOf()
        } else {
            val appDetailsHelper = AppDetailsHelper(authData)
            return appDetailsHelper.getAppByPackageName(
                packageNameList = saleBundle.sales.map { it.idandroid }
                    .toList()
            )
        }
    }
}
