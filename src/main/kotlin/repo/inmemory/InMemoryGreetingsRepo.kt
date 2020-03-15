package org.integrational.greetings.repo.inmemory

import org.integrational.greetings.domain.repo.GreetingEntity
import org.integrational.greetings.domain.repo.GreetingsRepo
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryGreetingsRepo : GreetingsRepo {
    companion object {
        private const val WORLD_NAME = "World"
        private val WORLD_KEY =
            key(
                WORLD_NAME
            )

        private fun key(name: String?): String = name?.toLowerCase() ?: WORLD_KEY
    }

    private val lastId = AtomicLong()
    private val entityById = mutableMapOf<Long, GreetingEntity>()
    private val idByName = mutableMapOf<String, Long>()

    init {
        createOrUpdate(
            GreetingEntity(
                null,
                WORLD_NAME, "Hello $WORLD_NAME"
            )
        )
    }

    /**
     * @throws DuplicateNameException
     */
    override fun createOrUpdate(entity: GreetingEntity): GreetingEntity = synchronized(this) {
        val name = entity.name
        val id = entity.id.let { id ->
            if (id == null) {
                // create new entity with a newly assigned id
                // no other entity must have that name
                findByName(name).let {
                    if (it != null) throw DuplicateNameException(
                        name
                    )
                }
                // use new id
                lastId.incrementAndGet()
            } else {
                // update the existing entity with the given id
                // no other entity must have that name, unless it's the entity being updated
                findByName(name).let {
                    if (it != null && it.id != id) throw DuplicateNameException(
                        name
                    )
                }
                // disassociate the entity's old name from its id
                read(id).let {
                    if (it != null) idByName -= it.name
                }
                // use existing id
                id
            }
        }
        val e = GreetingEntity(id, entity.name, entity.message)
        entityById[id] = e
        idByName[name] = id
        return e
    }

    override fun read(id: Long) = entityById[id]

    override fun delete(id: Long) = synchronized(this) {
        read(id).let {
            if (it == null) {
                // not found
                false
            } else {
                // found
                entityById -= id
                idByName -= it.name
                true
            }
        }
    }

    override fun findByName(name: String) = entityById[idByName[key(
        name
    )]]

    override fun findAll() = entityById.values
}

data class DuplicateNameException(val name: String) : Throwable("'$name' must be uique")