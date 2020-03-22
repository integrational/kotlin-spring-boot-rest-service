package org.integrational.greetings.domain.repo

import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.repo.inmemory.DuplicateNameException

interface GreetingsRepo {
    /**
     * Create a new persistent [Greeting] with a newly assigned [Greeting.id].
     * @throws DuplicateNameException If [greeting]`s [Greeting.name] is already used.
     * @throws IllegalArgumentException If [greeting]`s [Greeting.id] is not `null`.
     */
    fun create(greeting: Greeting): Greeting

    /**
     * Update the existing persistent [Greeting] with the given [greeting]'s [Greeting.id].
     * @throws DuplicateNameException If [greeting]`s [Greeting.name] is already used by another [Greeting].
     * @throws IllegalArgumentException If [greeting]`s [Greeting.id] is `null`.
     */
    fun update(greeting: Greeting): Greeting

    /**
     * Create a new or update an existing persistent [Greeting],
     * depending on whether the given [greeting]'s [Greeting.id] is `null` or not, respectively.
     * @throws DuplicateNameException If [greeting]`s [Greeting.name] is already used by another [Greeting].
     */
    fun createOrUpdate(greeting: Greeting) = if (greeting.id == null) create(greeting) else update(greeting)

    fun read(id: Long): Greeting?
    fun delete(id: Long): Boolean
    fun delete(greeting: Greeting): Boolean = greeting.id?.let { delete(it) } ?: false

    /** Find by name, which is a unique case-insensitive key. */
    fun findByName(name: String): Greeting?

    /** Find all, an return in no particular order. */
    fun findAll(): Collection<Greeting>
}