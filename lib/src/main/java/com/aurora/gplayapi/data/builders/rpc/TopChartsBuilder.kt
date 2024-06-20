package com.aurora.gplayapi.data.builders.rpc

object TopChartsBuilder {
    val TAG: String = javaClass.simpleName

    private const val TOKEN: String = TokenRepository.TOP_CHARTS_TOKEN
    fun build(category: String, chartType: String, tag: String = TAG): String {
        return """
            ["vyAe2","[$TOKEN,[2,\"$chartType\",\"$category\"]]]",null,"$tag@$category$chartType"]
        """
            .trim()
    }
}
