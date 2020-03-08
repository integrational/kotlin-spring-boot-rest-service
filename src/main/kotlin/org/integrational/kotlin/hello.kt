package org.integrational.kotlin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

data class Greeting(val id: Long, val message: String)

@RestController
class GreetingController {

    val lastId = AtomicLong()

    @GetMapping("/hello")
    fun hello() = greeting("World")

    @GetMapping("/hello/{name}")
    fun hello(@PathVariable("name") name: String) = greeting(name)

    private fun greeting(name: String) = Greeting(lastId.incrementAndGet(), name)
}