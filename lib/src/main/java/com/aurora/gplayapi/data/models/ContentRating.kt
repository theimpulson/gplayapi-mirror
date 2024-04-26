package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentRating(
    val title: String = String(),
    val description: String = String(),
    val recommendation: String = String(),
    val artwork: Artwork = Artwork(),
    val recommendationAndDescriptionHtml: String = String()
) : Parcelable
