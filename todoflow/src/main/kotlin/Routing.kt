package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.LoginRequest
import com.example.models.LoginResponse
import com.example.models.RegisterRequest
import com.example.models.UserResponse
import com.example.service.UserService
import com.mongodb.client.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }

    // Gọi cấu hình  routing
    configureCarsRouting()
    configAuthRouting()

}

fun Application.configAuthRouting(){
    val mongoDatabase = connectToMongoDB()
    val UserService = UserService(mongoDatabase)
    routing {
        route("/auth") {
            post("/register") {
                try {
                    println("Received register request")
                    val request = call.receive<RegisterRequest>()
                    println("Parsed request: $request")

                    val response = UserService.registerUser(request)
                    println("Response: $response")

                    call.respond(response)
                } catch (e: Exception) {
                    println("Error in register: ${e.message}")
                    e.printStackTrace()
                    call.respond(
                        HttpStatusCode.BadRequest,
                        LoginResponse(
                            success = false,
                            message = "Lỗi xử lý dữ liệu: ${e.message}"
                        )
                    )
                }
            }

            post("/login") {
                try {
                    println("Received login request")
                    val request = call.receive<LoginRequest>()
                    println("Parsed request: username=${request.username}")

                    val response = UserService.loginUser(request)
                    println("Response: $response")

                    call.respond(response)
                } catch (e: Exception) {
                    println("Error in login: ${e.message}")
                    e.printStackTrace()
                    call.respond(
                        HttpStatusCode.BadRequest,
                        LoginResponse(
                            success = false,
                            message = "Lỗi xử lý dữ liệu: ${e.message}"
                        )
                    )
                }
            }

            get("/profile/{id}") {
                try {
                    val userId = call.parameters["id"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "User ID is required"
                    )

                    val user = UserService.getUserById(userId)
                    if (user != null) {
                        call.respond(
                            UserResponse(
                                id = user.id!!,
                                username = user.username,
                                email = user.email
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: Exception) {
                    println("Error in profile: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "Server error")
                }
            }
        }
    }
}


fun Application.configureCarsRouting() {
    val mongoDatabase = connectToMongoDB()
    val carService = CarService(mongoDatabase)

    routing {
        // Create car
        post("/cars") {
//            val car = call.receive<Car>()
//            val id = carService.create(car)
//            call.respond(HttpStatusCode.Created, id)
            println("Received POST request to /cars")
            println("Content-Type: ${call.request.contentType()}")
            try {
                val car = call.receive<Car>()
                println("Parsed car: $car")
                val id = carService.create(car)
                call.respond(HttpStatusCode.Created, id)
            } catch (e: Exception) {
                println("Error: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
            }
        }

        // Read car
        get("/cars/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            carService.read(id)?.let { car ->
                call.respond(car)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        // Update car
        put("/cars/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            val car = call.receive<Car>()
            carService.update(id, car)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        // Delete car
        delete("/cars/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            carService.delete(id)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        // Route để thêm xe mẫu
        get("/add-sample-car") {
            val sampleCar = Car("Honda", "Civic", "XYZ789")
            val createdCar = carService.create(sampleCar)
            call.respondText("Đã thêm xe mẫu: $createdCar")
        }
    }
}