package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActiveDevice(
    val name: String = String(),
    val requiredOS: String = String()
) : Parcelable
