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
            call.respondText(
                text = "Hello World!",
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.OK
            )
        }
        get("/banner/json") {
            call.respondText(text = "{\"data\":[{\"desc\":\"我们支持订阅啦~\",\"id\":30,\"imagePath\":\"https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png\",\"isVisible\":1,\"order\":2,\"title\":\"我们支持订阅啦~\",\"type\":0,\"url\":\"https://www.wanandroid.com/blog/show/3352\"},{\"desc\":\"\",\"id\":6,\"imagePath\":\"https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png\",\"isVisible\":1,\"order\":1,\"title\":\"我们新增了一个常用导航Tab~\",\"type\":1,\"url\":\"https://www.wanandroid.com/navi\"},{\"desc\":\"一起来做个App吧\",\"id\":10,\"imagePath\":\"https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png\",\"isVisible\":1,\"order\":1,\"title\":\"一起来做个App吧\",\"type\":1,\"url\":\"https://www.wanandroid.com/blog/show/2\"}],\"errorCode\":0,\"errorMsg\":\"\"}",
            contentType = ContentType.Text.Plain,status = HttpStatusCode.OK)
        }
        get("/bean") {
            call.respond(status = HttpStatusCode.OK, HttpResult(data = "aloe"))
        }
    }
}