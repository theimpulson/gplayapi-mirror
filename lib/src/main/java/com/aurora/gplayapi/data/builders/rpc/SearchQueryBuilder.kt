package com.aurora.gplayapi.data.builders.rpc

internal object SearchQueryBuilder {
    val TAG: String = javaClass.simpleName

    private const val SEARCH_TOKEN: String = TokenRepository.SEARCH_TOKEN

    fun build(query: String, nextPageToken: String = "", tag: String = TAG): String {
        return if (nextPageToken.isNotEmpty()) {
            """
                ["qnKhOb","[[null,$SEARCH_TOKEN,null,\"$nextPageToken\"]]",null,"$tag@$query"]
            """.trim()
        } else {
            """
                ["lGYRle","[[[],$SEARCH_TOKEN,[\"$query\"],4,[null,1],null,null,[]]]",null,"$tag@$query"]
            """.trim()
        }
    }
}
