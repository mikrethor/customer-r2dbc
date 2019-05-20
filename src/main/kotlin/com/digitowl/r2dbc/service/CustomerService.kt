package com.digitowl.r2dbc.service

import com.digitowl.r2dbc.repository.CustomerRepository
import com.digitowl.r2dbc.domain.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomerService constructor(@Autowired private val customerRepository: CustomerRepository) {

    fun findCustomerById(id: Long): Mono<Customer> = customerRepository.findById(id)

    fun findCustomerFirstnameAndLastname(firstName: String, lastName: String): Mono<Customer> =
            customerRepository.findCustomerFirstnameAndLastname(firstName, lastName)

    //TODO rework to have update and create
    fun createCustomer(customer: Customer) = customerRepository.save(customer)

    fun findAll() = customerRepository.findAll()

}
