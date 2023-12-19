package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.providers.HeaderProvider
import com.aurora.gplayapi.network.IHttpClient

class DataSafetyHelper(authData: AuthData) : BaseHelper(authData) {

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    @Throws(Exception::class)
    fun getDataSafetyByPackageName(packageName: String) {
        val headers = HeaderProvider.getDefaultHeaders(authData)
        if (!headers.containsKey("Content-Type")) headers["Content-Type"] = "application/x-protobuf"


    }
}
