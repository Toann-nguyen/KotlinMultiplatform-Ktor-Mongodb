package org.toannguyen.kotlintodoflow.data.remote.api

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val brandName: String,
    val model: String,
    val number: String
)