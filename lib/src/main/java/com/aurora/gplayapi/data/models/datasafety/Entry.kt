package com.aurora.gplayapi.data.models.datasafety

import android.os.Parcelable
import com.aurora.gplayapi.data.models.Artwork
import kotlinx.parcelize.Parcelize

@Parcelize
data class Entry(
    var name: String,
    var description: String,
    var purpose: String = "",
    var artwork: Artwork = Artwork(),
    var subEntries: List<Entry> = listOf(),
    var data: List<Data> = listOf(),
    var type: EntryType = EntryType.UNKNOWN,
) : Parcelable {
    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Entry -> {
                name == other.name
            }

            else -> false
        }
    }
}