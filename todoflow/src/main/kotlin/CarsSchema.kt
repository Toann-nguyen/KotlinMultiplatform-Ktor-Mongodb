package com.example

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import org.bson.Document
import org.bson.types.ObjectId
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*

@Serializable
data class Car(
    val brandName: String,
    val model: String,
    val number: String
) {
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromDocument(document: Document): Car = json.decodeFromString(document.toJson())
    }
}

class CarService(private val database: MongoDatabase) {
    var collection: MongoCollection<Document>

    init {
        database.createCollection("cars")
        collection = database.getCollection("cars")
    }

    suspend fun addCar(database: MongoDatabase) {
        // Lấy collection "cars"
        val collection: MongoCollection<Document> = database.getCollection("cars")

        // Tạo một instance của Car
        val car = Car("Toyota", "Camry", "ABC123")

        // Chuyển Car thành Document
        val carDoc = Document("brandName", car.brandName)
            .append("model", car.model)
            .append("number", car.number)

        // Thêm document vào collection
        collection.insertOne(carDoc)

        // In ra ID của document vừa thêm
        println("Car added with id - ${carDoc["_id"]}")
    }

    // Create new car
    suspend fun create(car: Car): String = withContext(Dispatchers.IO) {
        val doc = car.toDocument()
        collection.insertOne(doc)
        (doc["_id"] as ObjectId).toHexString()
    }
    // Read a car
    suspend fun read(id: String): Car? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("_id", ObjectId(id))).first()?.let(Car::fromDocument)
    }

    // Update a car
    suspend fun update(id: String, car: Car): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndReplace(Filters.eq("_id", ObjectId(id)), car.toDocument())
    }

    // Delete a car
    suspend fun delete(id: String): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq("_id", ObjectId(id)))
    }
}

