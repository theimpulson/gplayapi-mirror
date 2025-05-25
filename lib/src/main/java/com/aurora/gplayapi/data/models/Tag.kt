package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Tag(
    val name: String,
    val url: String? = null,
) : Parcelable
