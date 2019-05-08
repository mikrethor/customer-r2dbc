package com.digitowl.r2dbc.config

import com.digitowl.r2dbc.domain.Customer
import com.digitowl.r2dbc.functionnal.CustomerHandler
import com.digitowl.r2dbc.functionnal.router
import com.digitowl.r2dbc.repository.CustomerRepository
import com.digitowl.r2dbc.service.CustomerService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import reactor.core.publisher.Flux

// See application.properties context.initializer.classes entry
class Beans : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) = beans {
        bean { CustomerHandler(ref()) }
        bean { CustomerService(ref()) }
        bean { router(ref()) }
        bean { init(ref()) }
    }.initialize(context)
}

internal fun init(repository: CustomerRepository) = CommandLineRunner {
    Flux.just(
            Customer(null, "Cinnamon", "Margetts"),
            Customer(null, "Anthea", "Scaddon"),
            Customer(null, "Colene", "Havenhand"))
            .flatMap { repository.save(it) }
            .thenMany(repository.findAll())
            .subscribe { println(it) }
}
