package com.aurora.gplayapi.data.builders.rpc

object SearchSuggestionQueryBuilder {
    public val TAG = javaClass.name
    fun build(query: String, tag: String = TAG): String {
        return """
            ["teXCtc","[null,[\"$query\"],[10],[2,1],4]",null,"$tag@$query"]
        """
            .trimStart()
            .trimEnd()
            .trimIndent()
    }
}
