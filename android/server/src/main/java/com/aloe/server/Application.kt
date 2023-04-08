/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.aloe.server

import com.aloe.server.module.BannerBean
import com.aloe.server.module.HttpResult
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(
        Netty,
        port = 8000,
        host = "0.0.0.0",
        module = Application::module,
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
        static("/static") { resources("public") }
        get("/") {
            call.respondText(
                text = "Hello World!",
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.OK,
            )
        }
        get("/text") {
            call.respondText(
                text = "aaaaaaaa",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.OK,
            )
        }
        get("/banner/json") {
            call.respond(status = HttpStatusCode.OK, HttpResult(data = getList()))
        }
        get("/data") {
            call.respond(status = HttpStatusCode.OK, HttpResult(data = "aloe"))
        }
    }
}

private fun getList() = "https://www.wanandroid.com".let {
    listOf(
        BannerBean(
            "30",
            0,
            1,
            2,
            "我们支持订阅啦~",
            "我们支持订阅啦~",
            "$it/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png",
            "$it/blog/show/3352",
        ),
        BannerBean(
            "6",
            1,
            1,
            1,
            "我们新增了一个常用导航Tab~",
            "",
            "$it/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png",
            "$it/navi",
        ),
        BannerBean(
            "10",
            1,
            1,
            1,
            "一起来做个App吧",
            "一起来做个App吧",
            "$it/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
            "$it/blog/show/2",
        )
    )
}
