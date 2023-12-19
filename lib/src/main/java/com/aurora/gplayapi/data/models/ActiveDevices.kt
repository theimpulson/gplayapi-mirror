package com.aurora.gplayapi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActiveDevices(
    val requiredOS: String = String(),
    val device: String = String()
) : Parcelable
