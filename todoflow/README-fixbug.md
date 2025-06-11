bị lỗi phần post("") cho các routing 

test Postman i 415 Unsupported Media Type


fix thêm
install(ContentNegotiation) {
json()
}

và thêm các routing

import io.ktor.server.plugins.contentnegotiation.*

import io.ktor.serialization.kotlinx.json.*

import io.ktor.server.request.*

import io.ktor.server.response.*

import io.ktor.http.*
