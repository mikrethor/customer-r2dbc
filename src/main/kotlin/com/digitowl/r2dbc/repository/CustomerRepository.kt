package com.digitowl.r2dbc.repository

import com.digitowl.r2dbc.domain.Customer
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.r2dbc.repository.query.Query
import reactor.core.publisher.Mono

interface CustomerRepository : R2dbcRepository<Customer, Long> {

    //Don't do select c from customer c where lastname = :lastname
    @Query("select id, lastname, firstname from Customer where firstName = :firstName and lastname = :lastName")
    fun findCustomerFisrtnameAndLastname(firstName: String, lastName: String): Mono<Customer>
}
