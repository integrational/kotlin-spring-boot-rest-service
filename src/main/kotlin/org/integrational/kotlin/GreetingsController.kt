package org.integrational.kotlin

import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class GreetingsController : GreetingsAPI {
    companion object {
        private const val NAME_WORLD = "World"
    }

    private val lastId = AtomicLong()
    private val greetings = mutableMapOf<String, Greeting>()

    constructor() {
        addGreeting(NAME_WORLD)
    }

    override fun getAll() = greetings.values.toTypedArray()

    override fun add(toAdd: GreetingToAdd) = addGreeting(toAdd.name)

    override fun getByName(name: String) = getGreeting(name)

    private fun addGreeting(name: String): Greeting {
        val nameKey = name.toLowerCase()
        if (nameKey !in greetings) {
            greetings[nameKey] = Greeting(lastId.incrementAndGet(), "Hello $name")
        }
        return greetings.getValue(nameKey)
    }

    private fun getGreeting(name: String): Greeting? = greetings[name.toLowerCase()]
}