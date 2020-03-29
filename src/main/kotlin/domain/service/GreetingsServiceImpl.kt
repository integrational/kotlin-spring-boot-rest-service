package org.integrational.greetings.domain.service

import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.domain.repo.GreetingsRepo
import org.springframework.stereotype.Service

@Service
class GreetingsServiceImpl(private val repo: GreetingsRepo) : GreetingsService {
    override fun add(name: String) = repo.createOrUpdate(Greeting(null, name, "Hello $name"))
    override fun get(name: String) = repo.findByName(name)
    override fun getAll() = repo.findAll()
}