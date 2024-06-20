package com.aurora.gplayapi.data.builders.rpc

object FeaturedStreamBuilder {
    val TAG: String = javaClass.simpleName

    private const val TOKEN: String = TokenRepository.STREAM_TOKEN
    fun build(category: String, tag: String = TAG): String {
        return """
            ["w3QCWb","[[null,2,\"${category}\",null,$TOKEN,null,2],[1,1]]",null,"$tag@$category"]
        """
            .trim()
    }
}
