package org.toannguyen.kotlintodoflow.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)