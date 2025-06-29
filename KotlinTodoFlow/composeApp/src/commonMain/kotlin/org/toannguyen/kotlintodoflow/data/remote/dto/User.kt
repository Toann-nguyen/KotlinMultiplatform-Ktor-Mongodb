package org.toannguyen.kotlintodoflow.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    val username: String,
    val email: String,
    val password: String? = null // Chỉ dùng khi tạo user, không trả về từ API
)
