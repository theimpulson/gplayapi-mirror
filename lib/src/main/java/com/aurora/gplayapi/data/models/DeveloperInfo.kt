package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeveloperInfo(
    val devId: String,
    var name: String = String(),
    var email: String = String(),
    var website: String = String(),
    var address: String = String()
) : Parcelable {
    override fun hashCode(): Int {
        return devId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is DeveloperInfo -> {
                devId == other.devId
            }

            else -> false
        }
    }
}

