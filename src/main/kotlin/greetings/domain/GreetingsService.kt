package greetings.domain

interface GreetingsService {
    fun add(name: String): Greeting
    fun get(name: String): Greeting?
    fun getAll(): Collection<Greeting>
}