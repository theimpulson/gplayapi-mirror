package com.aurora.gplayapi.utils

import android.util.Base64

object CertUtil {
    fun decodeHash(base64EncodedHash: String): String {
        // Replace characters that are invalid for a base64 string
        val standardBase64String = base64EncodedHash.replace('_', '+').replace('-', '/')

        // Decode the base64 string
        val decodedBytes = Base64.decode(standardBase64String, Base64.DEFAULT)

        // Convert the binary data to a hexadecimal string
        return bytesToHex(decodedBytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }
}
