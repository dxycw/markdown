package com.dxyc.markdown

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}

fun sayHello(platform: String): String {
    return "Hello $platform"
}
