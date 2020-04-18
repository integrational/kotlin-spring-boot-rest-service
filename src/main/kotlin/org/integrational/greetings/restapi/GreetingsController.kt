package org.integrational.greetings.restapi

import org.integrational.greetings.domain.repo.DuplicateNameException
import org.integrational.greetings.domain.service.GreetingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
class GreetingsController @Inject constructor(private val svc: GreetingsService) : GreetingsAPI {
    override fun add(toAdd: GreetingToAdd) = svc.add(toAdd.name)
    override fun getByName(name: String) = svc.get(name)
    override fun getAll() = svc.getAll()

    @ExceptionHandler
    fun handleDuplicateName(ex: DuplicateNameException): ResponseEntity<ErrorResponse> =
        ResponseEntity.badRequest().body(ErrorResponse("Duplicate name '${ex.name}'"))
}