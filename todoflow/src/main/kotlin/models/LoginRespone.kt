package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: UserResponse? = null,
    val token: String? = null
)