package greetings.repo

interface GreetingsRepo {
    fun createOrUpdate(entity: GreetingEntity): GreetingEntity
    fun read(id: Long): GreetingEntity?
    fun delete(id: Long): Boolean
    fun delete(entity: GreetingEntity): Boolean = entity.id?.let { delete(it) } ?: false

    /** Find by name, which is a unique key */
    fun findByName(name: String): GreetingEntity?

    /** Find all, in no particular order */
    fun findAll(): Collection<GreetingEntity>
}

data class GreetingEntity(val id: Long?, val name: String, val message: String)