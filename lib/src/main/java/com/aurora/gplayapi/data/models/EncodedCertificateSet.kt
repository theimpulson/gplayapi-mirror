package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EncodedCertificateSet(
    val certificateSet: String,
    val sha256: String
) : Parcelable
