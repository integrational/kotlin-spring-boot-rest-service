package greetings.domain

import java.util.concurrent.atomic.AtomicLong

class GreetingsServiceImpl : GreetingsService {
    companion object {
        private const val WORLD = "World"
    }

    private val lastId = AtomicLong()
    private val greetings = mutableMapOf<String, Greeting>()

    constructor() {
        add(WORLD)
    }

    override fun add(name: String): Greeting {
        val nameKey = name.toLowerCase()
        if (nameKey !in greetings) {
            greetings[nameKey] = Greeting(lastId.incrementAndGet(), "Hello $name")
        }
        return greetings.getValue(nameKey)
    }

    override fun get(name: String): Greeting? = greetings[name.toLowerCase()]

    override fun getAll(): Collection<Greeting> = greetings.values
}