package org.integrational.greetings.restapi

import org.integrational.greetings.domain.service.GreetingsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingsController(@Autowired private val svc: GreetingsService) : GreetingsAPI {
    override fun add(toAdd: GreetingToAdd) = svc.add(toAdd.name)
    override fun getByName(name: String) = svc.get(name)
    override fun getAll() = svc.getAll()
}