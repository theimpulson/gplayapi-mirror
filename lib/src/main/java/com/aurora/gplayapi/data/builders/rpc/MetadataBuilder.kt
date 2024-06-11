package com.aurora.gplayapi.data.builders.rpc

object MetadataBuilder {
    val TAG: String = javaClass.simpleName

    private const val TOKEN: String =
        "null,null,[[1,9,10,11,13,14,19,20,38,43,47,49,52,58,59,63,69,70,73,74,75,78,79,80,91,92,95,96,97,100,101,103,106,112,119,129,137,139,141,145,146,151,155]],[[[1,null,1],null,[[[]]],null,null,null,null,[null,2],null,null,null,null,null,null,null,null,null,null,null,null,null,null,[1]],[null,[[[]]],null,null,[1]],[null,[[[]]],null,[1]],[null,[[[]]]],null,null,null,null,[[[[]]]],[[[[]]]]],null"

    fun build(packageName: String, tag: String = TAG): String {
        return """
            ["Ws7gDc","[${TOKEN},[[\"${packageName}\",7]]]",null,"${TAG}@${packageName}"]
        """
            .trim()
    }
}
