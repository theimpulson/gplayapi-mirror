package com.aurora.gplayapi.helpers.web

import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.contracts.CategoryStreamContract
import com.aurora.gplayapi.helpers.contracts.StreamContract
import com.aurora.gplayapi.network.IHttpClient
import com.aurora.gplayapi.utils.CategoryUtil

class WebCategoryStreamHelper : BaseWebHelper(), CategoryStreamContract {
    private lateinit var webStreamHelper: WebStreamHelper

    init {
        this.webStreamHelper = WebStreamHelper()
    }

    override fun using(httpClient: IHttpClient) = apply {
        this.httpClient = httpClient
        this.webStreamHelper = WebStreamHelper().using(httpClient)
    }

    override fun fetch(url: String): StreamBundle {
        val category = CategoryUtil.getCategoryFromUrl(url)
        return webStreamHelper.fetch(StreamContract.Type.HOME, category)
    }

    override fun nextStreamCluster(nextPageUrl: String): StreamCluster {
        return webStreamHelper.nextStreamCluster(nextPageUrl)
    }

    override fun nextStreamBundle(
        category: StreamContract.Category,
        nextPageToken: String
    ): StreamBundle {
        return webStreamHelper.nextStreamBundle(category, nextPageToken)
    }
}
