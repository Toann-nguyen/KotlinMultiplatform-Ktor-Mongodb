package com.example

import com.example.infrastructure.config.configureMonitoring
import com.example.infrastructure.config.configureSecurity
import com.example.infrastructure.config.configureSerialization
import com.example.infrastructure.database.configureDatabases
import com.example.presentation.routes.configAuthRouting
import com.example.presentation.routes.configureCarsRouting
import com.example.presentation.routes.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

}

fun Application.module() {

    configureMonitoring()
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureCarsRouting()
    configAuthRouting()
}