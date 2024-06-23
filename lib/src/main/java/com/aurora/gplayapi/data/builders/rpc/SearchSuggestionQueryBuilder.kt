package com.aurora.gplayapi.data.builders.rpc

internal object SearchSuggestionQueryBuilder {
    val TAG: String = javaClass.simpleName

    fun build(query: String, tag: String = TAG): String {
        return """
            ["teXCtc","[null,[\"$query\"],[10],[2,1],4]",null,"$tag@$query"]
        """
            .trim()
    }
}
