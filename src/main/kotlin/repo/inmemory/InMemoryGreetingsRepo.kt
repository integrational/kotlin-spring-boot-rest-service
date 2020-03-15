package org.integrational.greetings.repo.inmemory

import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.domain.repo.GreetingsRepo
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicLong

@Repository
final class InMemoryGreetingsRepo : GreetingsRepo {
    companion object {
        private const val WORLD_NAME = "World"
        private val WORLD_KEY = key(WORLD_NAME)

        private fun key(name: String?): String = name?.toLowerCase() ?: WORLD_KEY
        private fun greeting(entity: GreetingEntity): Greeting = Greeting(entity.id, entity.name, entity.message)
    }

    private val lastId = AtomicLong() // sequence generating primary key
    private val entityById = mutableMapOf<Long, GreetingEntity>() // primary repo
    private val idByName = mutableMapOf<String, Long>() // index for unique key 'name'

    init {
        create(Greeting(null, WORLD_NAME, "Hello $WORLD_NAME"))
    }

    override fun create(greeting: Greeting): Greeting = synchronized(this) {
        if (greeting.id != null) throw IllegalArgumentException("id of Greeting to create must be null")

        // no other entity must have that name
        findByName(greeting.name)?.let { throw DuplicateNameException(greeting.name) }

        // use new id
        return storeAndTransform(lastId.incrementAndGet(), greeting.name, greeting.message)
    }

    override fun update(greeting: Greeting): Greeting = synchronized(this) {
        if (greeting.id == null) throw IllegalArgumentException("id of Greeting to update must not be null")

        // no other entity must have that name, unless it's the entity being updated
        findByName(greeting.name)?.takeIf { it.id != greeting.id }?.let { throw DuplicateNameException(greeting.name) }

        // disassociate the entity's old name from its id
        read(greeting.id)?.let { idByName -= it.name }

        // use existing id
        return storeAndTransform(greeting.id, greeting.name, greeting.message)
    }

    private fun storeAndTransform(id: Long, name: String, message: String): Greeting {
        val ge = GreetingEntity(id, name, message)
        entityById[id] = ge
        idByName[name] = id
        return greeting(ge)
    }

    override fun read(id: Long) = entityById[id]?.let { greeting(it) }

    override fun delete(id: Long) = synchronized(this) {
        read(id)?.let {
            // found
            entityById -= id
            idByName -= it.name
            true
        } ?: false // not found
    }

    override fun findByName(name: String) = entityById[idByName[key(name)]]?.let { greeting(it) }

    override fun findAll() = entityById.values.map { greeting(it) }
}

data class DuplicateNameException(val name: String) : Throwable("'$name' must be uique")

private data class GreetingEntity(val id: Long?, val name: String, val message: String)
