package org.integrational.greetings

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.Logger

@SpringBootApplication
class Application constructor(
    @Value("\${app}") private val app: String,
    @Value("\${env}") private val env: String
) {
    private val LOG = Logger.getLogger(Application::class.java.canonicalName)

    init {
        LOG.info("Starting $app in $env")
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
