package com.example.domain.entities

import kotlinx.serialization.Serializable
import org.bson.Document
import java.security.MessageDigest

@Serializable
data class User(
    val id: String? = null,
    val username: String,
    val email: String,
    val passwordHash: String? = null
) {
    fun toDocument(): Document {
        val doc = Document("username", username)
            .append("email", email)
        passwordHash?.let { doc.append("passwordHash", it) }
        return doc
    }

    companion object {
        fun fromDocument(document: Document): User = User(
            id = document.getObjectId("_id")?.toHexString(),
            username = document.getString("username"),
            email = document.getString("email"),
            passwordHash = document.getString("passwordHash")
        )

        fun hashPassword(password: String): String {
            return MessageDigest.getInstance("SHA-256")
                .digest(password.toByteArray())
                .joinToString("") { "%02x".format(it) }
        }
    }
}