package com.aloe.server

import com.aloe.server.module.HttpResult
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(
        Netty,
        port = 8000,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureTemplating()
    configureSerialization()
}

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureRouting() {
    routing {
        static("/static") {
            resources("public")
        }
        get("/") {
            call.respondText(text = "Hello World!", contentType = ContentType.Text.Html, status = HttpStatusCode.OK)
        }
        get("/bean") {
            call.respond(status = HttpStatusCode.OK, HttpResult(data = "aloe"))
        }
    }
}