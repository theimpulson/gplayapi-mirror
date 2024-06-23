package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.builders.WebAppBuilder
import com.aurora.gplayapi.data.builders.rpc.MetadataBuilder
import com.aurora.gplayapi.data.builders.rpc.RelatedAppsBuilder
import com.aurora.gplayapi.data.builders.rpc.RpcBuilder
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.contracts.AppDetailsContract
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.dig

class WebAppDetailsHelper : BaseWebHelper(), AppDetailsContract {

    override fun getAppByPackageName(packageName: String): App {
        val apps = getAppByPackageName(listOf(packageName))
        return if (apps.isNotEmpty()) {
            apps.first()
        } else {
            App(packageName)
        }
    }

    override fun getAppByPackageName(packageNameList: List<String>): List<App> {
        val requests = packageNameList.map { packageName -> MetadataBuilder.build(packageName) }
        val response = WebClient(httpClient).fetch(requests.toTypedArray()).let {
            RpcBuilder.wrapResponse(it)
        }

        val apps: MutableList<App> = mutableListOf()

        packageNameList.forEach { packageName ->
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

    // TODO: Unify with AppDetailsHelper
    fun getRelatedClusters(packageName: String): List<StreamCluster> {
        val payload = execute(RelatedAppsBuilder.build(packageName))
        val relatedPayload = payload.dig<List<Any>>(RelatedAppsBuilder.TAG, packageName, 1, 1)

        if (relatedPayload.isNullOrEmpty()) {
            return listOf()
        }

        val relatedClusters = mutableListOf<StreamCluster>()

        relatedPayload.forEach {
            relatedClusters.add(parseCluster(it, 21, arrayOf(0, 0)))
        }

        return relatedClusters
    }

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
    }
}
