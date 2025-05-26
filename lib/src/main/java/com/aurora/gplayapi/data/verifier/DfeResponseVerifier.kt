package com.aurora.gplayapi.data.verifier

import android.util.Base64
import java.security.SecureRandom

object DfeResponseVerifier {
    private val secureRandomInstance: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
    private val nonceBytes = ByteArray(256)

    @Synchronized
    fun generateNonce(): String {
        secureRandomInstance.nextBytes(nonceBytes)

        return "nonce=" + Base64.encodeToString(
            nonceBytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
}