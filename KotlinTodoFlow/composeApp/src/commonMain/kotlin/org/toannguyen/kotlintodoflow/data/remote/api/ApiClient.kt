package org.toannguyen.kotlintodoflow.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.toannguyen.kotlintodoflow.data.remote.dto.LoginRequest
import org.toannguyen.kotlintodoflow.data.remote.dto.LoginResponse
import org.toannguyen.kotlintodoflow.data.remote.dto.RegisterRequest

class ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val baseUrl = "http://10.0.2.2:9000"

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return try {
            client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }.body()
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Network error: ${e.message}"
            )
        }
    }

    suspend fun register(registerRequest: RegisterRequest): LoginResponse {
        return try {
            client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }.body()
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Network error: ${e.message}"
            )
        }
    }

    fun close() {
        client.close()
    }
}