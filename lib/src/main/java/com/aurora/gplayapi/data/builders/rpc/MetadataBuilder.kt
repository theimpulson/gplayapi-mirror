package com.aurora.gplayapi.data.builders.rpc

internal object MetadataBuilder {
    val TAG: String = javaClass.simpleName

    private const val TOKEN: String = TokenRepository.METADATA_TOKEN
    fun build(packageName: String, tag: String = TAG): String {
        return """
            ["Ws7gDc","[${TOKEN},[[\"${packageName}\",7]]]",null,"${tag}@${packageName}"]
        """
            .trim()
    }
}
