package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.WebAppBuilder
import com.aurora.gplayapi.data.builders.rpc.MetadataBuilder
import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.network.DefaultHttpClient
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig

class WebAppDetailsHelper {
    private var httpClient: IHttpClient = DefaultHttpClient

    fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }

    private fun execute(freq: Array<String>): HashMap<String, HashMap<String, Any>> {
        return WebClient(httpClient)
            .fetch(freq)
            .let {
                RpcBuilder.wrapResponse(it)
            }
    }

    fun getAppByPackageName(packageNames: List<String>): MutableList<App> {
        val requests = packageNames.map { packageName -> MetadataBuilder.build(packageName) }
        val response = execute(requests.toTypedArray())
        val apps: MutableList<App> = mutableListOf()

        packageNames.forEach { packageName ->
            val payload = response.dig<Any>(MetadataBuilder.TAG, packageName)
            if (payload != null) {
                apps.add(
                    WebAppBuilder.build(
                        packageName,
                        payload
                    )
                )
            }
        }

        apps.apply {
            filter {
                it.displayName.isNotEmpty()
            }
        }

        return apps
    }
}
