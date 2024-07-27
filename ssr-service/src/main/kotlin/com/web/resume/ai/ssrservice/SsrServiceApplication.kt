package com.web.resume.ai.ssrservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SsrServiceApplication

fun main(args: Array<String>) {
    runApplication<SsrServiceApplication>(*args)
}

@RestController
class GreetingController {
    @GetMapping("/")
    fun greeting(): String {
        return "Hello from Cloud Run!"
    }
}