package org.toannguyen.kotlintodoflow

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform