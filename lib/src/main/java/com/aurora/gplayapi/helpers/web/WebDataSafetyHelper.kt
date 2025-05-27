/*
 * SPDX-FileCopyrightText: 2024 Aurora OSS
 * SPDX-FileCopyrightText: 2024-2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.WebAppBuilder.parseArtwork
import com.aurora.gplayapi.data.builders.rpc.DataSafetyBuilder
import com.aurora.gplayapi.data.models.DeveloperInfo
import com.aurora.gplayapi.data.models.datasafety.Data
import com.aurora.gplayapi.data.models.datasafety.Entry
import com.aurora.gplayapi.data.models.datasafety.EntryType
import com.aurora.gplayapi.data.models.datasafety.Report
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig
import com.aurora.gplayapi.utils.digOrDefault
import java.util.Locale

class WebDataSafetyHelper : BaseWebHelper() {

    override fun with(locale: Locale) = apply {
        this.locale = locale
    }

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    fun fetch(packageName: String): Report {
        val response = execute(DataSafetyBuilder.build(packageName))
        val dataSafetyPayload = response.dig<List<Any>>(DataSafetyBuilder.TAG, packageName, 1, 2)

        val subEntries = dataSafetyPayload.dig<List<Any>>(137, 4)

        return Report(
            packageName = packageName,
            developerInfo = parseDevInfo(dataSafetyPayload),
            artwork = parseArtwork(dataSafetyPayload.dig(95, 0), 4),
            privacyUrl = dataSafetyPayload.dig<String>(99, 0, 5, 2),
            entries = mutableListOf(
                parseEntry(EntryType.DATA_SHARED, subEntries.dig(0)),
                parseEntry(EntryType.DATA_COLLECTED, subEntries.dig(1)),
                parseSecurityEntry(
                    EntryType.SECURITY_PRACTICES,
                    dataSafetyPayload.dig<List<Any>>(137, 9)
                )
            )
        )
    }

    private fun parseDevInfo(payload: List<Any>): DeveloperInfo {
        var developerId = payload.dig<String>(68, 2)

        if (developerId.isBlank()) {
            developerId = payload.digOrDefault<String>("unknown", 68, 0)
        }

        return DeveloperInfo(
            devId = developerId,
            name = payload.dig<String>(68, 0),
            website = payload.dig<String>(69, 0, 5, 2),
            email = payload.dig<String>(69, 1, 0),
            address = payload.dig<String>(69, 2, 0)
        )
    }

    private fun parseEntry(entryType: EntryType, payload: List<Any>): Entry {
        val subEntries = payload.dig<List<List<Any>>>(0)
        return Entry(
            type = entryType,
            name = payload.dig<String>(1),
            description = payload.dig<String>(6, 1),
            purpose = payload.dig<String>(3),
            artwork = parseArtwork(payload.dig<List<Any>>(5), 4),
            subEntries = subEntries.map { parseSubEntry(it) }
        )
    }

    private fun parseSubEntry(payload: List<Any>): Entry {
        val entries = payload.dig<List<List<Any>>>(4)
        return Entry(
            name = payload.dig<String>(0, 1),
            description = payload.dig<String>(0, 2, 1),
            artwork = parseArtwork(payload.dig(0, 0), 4),
            data = entries.map { d -> parseData(d) }
        )
    }

    private fun parseSecurityEntry(entryType: EntryType, payload: List<Any>): Entry {
        val entries = payload.dig<List<List<Any>>>(2)
        return Entry(
            type = entryType,
            name = payload.dig<String>(1),
            description = "",
            artwork = parseArtwork(payload.dig(0), 4),
            subEntries = entries.map { d ->
                Entry(
                    name = d.dig<String>(1),
                    description = d.dig<String>(2, 1),
                    purpose = "",
                    artwork = parseArtwork(d.dig(0), 4)
                )
            }
        )
    }

    private fun parseData(payload: List<Any>): Data {
        return Data(
            name = payload.dig<String>(0),
            reason = payload.dig<String>(2),
            optional = payload.dig<Boolean>(1) == true
        )
    }
}
