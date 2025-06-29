package org.toannguyen.kotlintodoflow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null,
    val token: String? = null
)