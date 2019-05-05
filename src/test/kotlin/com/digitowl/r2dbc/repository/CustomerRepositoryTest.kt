package com.digitowl.r2dbc.repository

import com.digitowl.r2dbc.DbConfig
import com.digitowl.r2dbc.domain.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DbConfig::class])
class CustomerRepositoryTest(@Autowired
                             val customerRepository: CustomerRepository) {
    var id: Long = 1

    @BeforeEach
    fun setUp() {
        StepVerifier.create(customerRepository.deleteAll()).verifyComplete()
        val customerById = Customer(lastname = "nom by id", firstname = "firstname by id")
        StepVerifier.create(customerRepository.save(customerById))
                .expectNext(customerById)
                .verifyComplete()
        id = customerById.id!!
        val customerByName = Customer(lastname = "lastname", firstname = "firstname")
        StepVerifier.create(customerRepository.save(customerByName))
                .expectNext(customerByName)
                .verifyComplete()
    }

    @Test
    fun `Find a customer by his firstname and his lastname`() {
        StepVerifier.create(customerRepository.findCustomerFirstnameAndLastname("firstname", "lastname"))
                .assertNext { c -> Assertions.assertThat("lastname").isEqualTo(c.lastname) }.verifyComplete()
    }

    @Test
    fun `Find a customer by his id`() {
        StepVerifier.create(customerRepository.findById(id))
                .assertNext { c -> Assertions.assertThat("nom by id").isEqualTo(c.lastname) }
                .verifyComplete()
    }

    @Test
    fun `Save a customer`() {
        val id: Long? = null
        val client = Customer(id, "firstname 2", "nom 2")
        //save with id is an update so id must be null in order to have an insert
        StepVerifier.create(customerRepository.save(client))
                .expectNext(client)
                .verifyComplete()
        StepVerifier.create(customerRepository.findCustomerFirstnameAndLastname("firstname 2", "nom 2"))
                .assertNext {

                    c ->
                    // not working
                    //Assertions.assertThat(c === client).isEqualTo(true)
                    //Assertions.assertThat(c == client).isEqualTo(true)

                    run {
                        Assertions.assertThat("nom 2").isEqualTo(c.lastname)
                        Assertions.assertThat("firstname 2").isEqualTo(c.firstname)
                        Assertions.assertThat(client.id).isEqualTo(c.id)
                    }
                }
                .verifyComplete()
    }
}