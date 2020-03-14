package greetings.domain

interface GreetingsService {
    fun add(name: String): Greeting
    fun get(name: String): Greeting?
    fun getAll(): Collection<Greeting>
}

data class Greeting(val id: Long, val message: String)
