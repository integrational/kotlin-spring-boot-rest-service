package org.integrational.greetings.restapi

import org.integrational.greetings.domain.model.Greeting
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RequestMapping("/greetings", produces = [APPLICATION_JSON_VALUE])
interface GreetingsAPI {
    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    fun add(@RequestBody toAdd: GreetingToAdd): Greeting

    @GetMapping("/{name}")
    fun getByName(@PathVariable("name") name: String): Greeting?

    @GetMapping
    fun getAll(): Collection<Greeting>
}

data class GreetingToAdd(val name: String)

data class ErrorResponse(val message: String)