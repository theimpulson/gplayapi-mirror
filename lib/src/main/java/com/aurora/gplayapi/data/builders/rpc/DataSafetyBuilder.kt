package com.aurora.gplayapi.data.builders.rpc

internal object DataSafetyBuilder {
    val TAG: String = javaClass.simpleName

    fun build(packageName: String, tag: String = TAG): String {
        return """
            ["Ws7gDc","${TokenRepository.DATA_SAFETY_TOKEN.format(packageName)}",null,"${tag}@${packageName}"]
        """
            .trim()
    }
}
