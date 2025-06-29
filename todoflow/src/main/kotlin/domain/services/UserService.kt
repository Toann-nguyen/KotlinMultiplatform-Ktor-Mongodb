package com.example.domain.services

import com.example.domain.entities.User
import com.example.application.dto.request.LoginRequest
import com.example.application.dto.respone.LoginResponse
import com.example.application.dto.request.RegisterRequest
import com.example.application.dto.respone.UserResponse
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId

class UserService(private val database: MongoDatabase) {
    private var collection: MongoCollection<Document>

    init {
        try {
            database.createCollection("users")
        } catch (e: Exception) {
            println("Collection 'users' may already exist: ${e.message}")
        }
        collection = database.getCollection("users")
    }

    suspend fun registerUser(request: RegisterRequest): LoginResponse = withContext(Dispatchers.IO) {
        try {
            // Kiểm tra username đã tồn tại
            val existingUser = collection.find(Filters.eq("username", request.username)).first()
            if (existingUser != null) {
                return@withContext LoginResponse(
                    success = false,
                    message = "Tên đăng nhập đã tồn tại"
                )
            }

            // Kiểm tra email đã tồn tại
            val existingEmail = collection.find(Filters.eq("email", request.email)).first()
            if (existingEmail != null) {
                return@withContext LoginResponse(
                    success = false,
                    message = "Email đã được sử dụng"
                )
            }

            // Tạo user mới
            val passwordHash = User.Companion.hashPassword(request.password)
            val user = User(
                username = request.username,
                email = request.email,
                passwordHash = passwordHash
            )

            val doc = user.toDocument()
            collection.insertOne(doc)
            val userId = (doc["_id"] as ObjectId).toHexString()

            LoginResponse(
                success = true,
                message = "Đăng ký thành công",
                user = UserResponse(
                    id = userId,
                    username = user.username,
                    email = user.email
                ),
                token = "simple_token_$userId" // Simple token for demo
            )
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Lỗi server: ${e.message}"
            )
        }
    }
    suspend fun loginUser(request: LoginRequest): LoginResponse = withContext(Dispatchers.IO) {
        try {
            val passwordHash = User.Companion.hashPassword(request.password)
            val document = collection.find(
                Filters.and(
                    Filters.eq("username", request.username),
                    Filters.eq("passwordHash", passwordHash)
                )
            ).first()

            if (document != null) {
                val user = User.Companion.fromDocument(document)
                LoginResponse(
                    success = true,
                    message = "Đăng nhập thành công",
                    user = UserResponse(
                        id = user.id!!,
                        username = user.username,
                        email = user.email
                    ),
                    token = "simple_token_${user.id}"
                )
            } else {
                LoginResponse(
                    success = false,
                    message = "Tên đăng nhập hoặc mật khẩu không đúng"
                )
            }
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Lỗi server: ${e.message}"
            )
        }
    }

    suspend fun getUserById(id: String): User? = withContext(Dispatchers.IO) {
        try {
            collection.find(Filters.eq("_id", ObjectId(id))).first()?.let(User.Companion::fromDocument)
        } catch (e: Exception) {
            null
        }
    }
}