package com.example.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import org.bson.Document
import org.bson.types.ObjectId
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
