package org.toannguyen.kotlintodoflow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)