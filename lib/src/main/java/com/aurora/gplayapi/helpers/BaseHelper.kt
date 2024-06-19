package com.aurora.gplayapi.helpers

import com.aurora.gplayapi.network.DefaultHttpClient
import com.aurora.gplayapi.network.IHttpClient

abstract class BaseHelper {
    var httpClient: IHttpClient = DefaultHttpClient

    abstract fun using(httpClient: IHttpClient): BaseHelper
}