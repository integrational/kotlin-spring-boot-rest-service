package org.integrational.greetings.domain.service

import org.integrational.greetings.domain.gender.Gender.*
import org.integrational.greetings.domain.gender.GenderService
import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.domain.repo.GreetingsRepo
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * [GreetingsService] domain service implementation.
 */
@Named
@Singleton
class GreetingsServiceImpl @Inject constructor(private val repo: GreetingsRepo, private val genderSvc: GenderService) :
    GreetingsService {
    override fun add(name: String) = repo.createOrUpdate(Greeting(null, name, message(name)))

    private fun message(name: String): String {
        val gender = genderSvc.genderOfName(name)
        val salutation = when (gender) {
            FEMALE -> "Mrs "
            MALE -> "Mr "
            UNSPECIFIED -> ""
        }
        return "Hello $salutation$name!"
    }

    override fun get(name: String) = repo.findByName(name)
    override fun getAll() = repo.findAll()
}