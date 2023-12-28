package com.aurora.gplayapi.utils

import android.util.Base64

object CertUtil {

    @OptIn(ExperimentalStdlibApi::class)
    fun decodeHash(base64EncodedHash: String): String {
        return Base64.decode(base64EncodedHash, Base64.URL_SAFE).toHexString()
    }
}
