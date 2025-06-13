package org.toannguyen.kotlintodoflow.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null,
    val token: String? = null
)