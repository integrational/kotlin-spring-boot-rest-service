package org.integrational.kotlin

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping("/greetings", produces = [MediaType.APPLICATION_JSON_VALUE])
interface GreetingsAPI {
    @GetMapping
    fun getAll(): Array<Greeting>

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun add(@RequestBody toAdd: GreetingToAdd): Greeting

    @GetMapping("/{name}")
    fun getByName(@PathVariable("name") name: String): Greeting?
}

data class GreetingToAdd(val name: String)
data class Greeting(val id: Long, val message: String)
