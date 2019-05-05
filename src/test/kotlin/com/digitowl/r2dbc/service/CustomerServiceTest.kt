package com.digitowl.r2dbc.service

import com.digitowl.r2dbc.domain.Customer
import com.digitowl.r2dbc.repository.CustomerRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class CustomerServiceTest {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `Find a customer by his firstname and his lastname`() {
        val repository = mockk<CustomerRepository>()
        val customer = Customer(id = 1, firstname = "Meggy", lastname = "Birds")
        every { repository.findById(1) } returns Mono.just(customer)
        val service = CustomerService(repository)

        StepVerifier.create(service.findCustomerById(1))
                .expectNext(customer)
                .verifyComplete()
    }

    @Test
    fun `Find a customer by his id`() {
        val repository = mockk<CustomerRepository>()
        val customer = Customer(id = 2, firstname = "Odo", lastname = "Shankster")
        every { repository.findCustomerFirstnameAndLastname("Odo", "Shankster") } returns Mono.just(customer)
        val service = CustomerService(repository)

        StepVerifier.create(service.findCustomerFirstnameAndLastname("Odo", "Shankster"))
                .expectNext(customer)
                .verifyComplete()
    }

    @Test
    fun `Save a customer`() {
        val repository = mockk<CustomerRepository>()
        val customer = Customer(id = null, firstname = "Lyn", lastname = "Jeannenet")
        val saveCustomer = Customer(id = 3, firstname = "Lyn", lastname = "Jeannenet")
        every { repository.save(customer) } returns Mono.just(saveCustomer)
        val service = CustomerService(repository)

        StepVerifier.create(service.createCustomer(customer))
                .expectNext(saveCustomer)
                .verifyComplete()
    }
}