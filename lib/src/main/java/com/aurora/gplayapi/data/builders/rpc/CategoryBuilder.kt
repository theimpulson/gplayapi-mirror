package com.aurora.gplayapi.data.builders.rpc

object CategoryBuilder {
    val TAG: String = javaClass.simpleName

    fun build(tag: String = TAG): String {
        return """
            ["KT5WVe","[1]",null,"${tag}@${tag}"]
        """
            .trim()
    }
}
