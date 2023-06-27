package com.aurora.gplayapi.data.builders.rpc

object SearchQueryBuilder {
    public val TAG = javaClass.name
    fun build(query: String, tag: String = TAG): String {
        return """
            ["lGYRle","[[[],[[8,[20,50]],null,null,[96,108,72,100,27,183,8,57,169,110,11,184,16,1,139,152,194,165,68,163,211,9,71,31,195,12,64,151,150,148,113,104,55,56,145,32,34,10,122],[],null,null,[[[1,2],[10,8,9]]]],[\"$query\"],4,[null,1],null,null,[],[1]],[1]]",null,"$tag@$query"]
        """
            .trimStart()
            .trimEnd()
            .trimIndent()
    }
}
