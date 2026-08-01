package com.dxyc.markdown

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform