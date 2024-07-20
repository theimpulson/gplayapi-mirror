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

class WebDataSafetyHelper : BaseWebHelper() {
    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    fun fetch(packageName: String): Report {
        val response = execute(DataSafetyBuilder.build(packageName))
        val dataSafetyPayload =
            response.dig<Collection<Any>>(DataSafetyBuilder.TAG, packageName, 1, 2)
                ?: return Report(packageName)

        val subEntries: Collection<Any> = dataSafetyPayload.dig(137, 4) ?: arrayListOf()

        return Report(packageName).apply {
            developerInfo = parseDevInfo(dataSafetyPayload)
            artwork = parseArtwork(dataSafetyPayload.dig(95, 0) ?: arrayListOf(), 4)
            privacyUrl = dataSafetyPayload.dig(99, 0, 5, 2) ?: ""
            entries = mutableListOf(
                parseEntry(EntryType.DATA_SHARED, subEntries.dig(0) ?: arrayListOf()),
                parseEntry(EntryType.DATA_COLLECTED, subEntries.dig(1) ?: arrayListOf()),
                parseSecurityEntry(
                    EntryType.SECURITY_PRACTICES,
                    dataSafetyPayload.dig(137, 9) ?: arrayListOf()
                )
            )
        }
    }

    private fun parseDevInfo(payload: Collection<Any>): DeveloperInfo {
        val devId = payload.dig<String>(68, 2) ?: payload.dig<String>(68, 0) ?: "unknown"
        return DeveloperInfo(devId).apply {
            name = payload.dig(68, 0) ?: ""
            website = payload.dig(69, 0, 5, 2) ?: ""
            email = payload.dig(69, 1, 0) ?: ""
            address = payload.dig(69, 2, 0) ?: ""
        }
    }

    private fun parseEntry(entryType: EntryType, payload: Collection<Any>): Entry {
        val subEntries: Collection<Collection<Any>> = payload.dig(0) ?: arrayListOf()
        return Entry(
            type = entryType,
            name = payload.dig(1) ?: "",
            description = payload.dig(6, 1) ?: "",
            purpose = payload.dig(3) ?: "",
            artwork = parseArtwork(payload.dig(5) ?: arrayListOf(), 4),
            subEntries = subEntries.map { parseSubEntry(it) }
        )
    }

    private fun parseSubEntry(payload: Collection<Any>): Entry {
        val entries: Collection<Collection<Any>> = payload.dig(4) ?: arrayListOf()
        return Entry(
            name = payload.dig(0, 1) ?: "",
            description = payload.dig(0, 2, 1) ?: "",
            artwork = parseArtwork(payload.dig(0, 0) ?: arrayListOf(), 4),
            data = entries.map { d -> parseData(d) }
        )
    }

    private fun parseSecurityEntry(entryType: EntryType, payload: Collection<Any>): Entry {
        val entries: Collection<Collection<Any>> = payload.dig(2) ?: arrayListOf()
        return Entry(
            type = entryType,
            name = payload.dig(1) ?: "",
            description = "",
            artwork = parseArtwork(payload.dig(0) ?: arrayListOf(), 4),
            subEntries = entries.map { d ->
                Entry(
                    name = d.dig(1) ?: "",
                    description = d.dig(2, 1) ?: "",
                    purpose = "",
                    artwork = parseArtwork(d.dig(0) ?: arrayListOf(), 4)
                )
            }
        )
    }

    private fun parseData(payload: Collection<Any>): Data {
        return Data(
            name = payload.dig(0) ?: "",
            reason = payload.dig(2) ?: "",
            optional = payload.dig(1) ?: false
        )
    }
}
