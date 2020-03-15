package org.integrational.greetings.domain.service

import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.domain.repo.GreetingEntity
import org.integrational.greetings.domain.repo.GreetingsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GreetingsServiceImpl(@Autowired val repo: GreetingsRepo) : GreetingsService {
    override fun add(name: String) =
        greeting(
            repo.createOrUpdate(GreetingEntity(null, name, "Hello $name"))
        )

    override fun get(name: String) = repo.findByName(name).let {
        if (it == null) null else greeting(it)
    }

    override fun getAll() = repo.findAll().map {
        greeting(
            it
        )
    }

    companion object {
        private const val MISSING_ID = -1L

        private fun greeting(e: GreetingEntity): Greeting =
            Greeting(
                e.id ?: MISSING_ID, e.message
            )
    }
}