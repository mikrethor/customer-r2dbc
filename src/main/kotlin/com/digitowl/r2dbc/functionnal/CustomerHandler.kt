package com.digitowl.r2dbc.functionnal

import com.digitowl.r2dbc.domain.Customer
import com.digitowl.r2dbc.service.CustomerService
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

class CustomerHandler(private val service: CustomerService) {

    @Suppress("UNUSED_PARAMETER")
    fun findAll(request: ServerRequest) = ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(service.findAll(), Customer::class.java)

    fun findCustomerById(request: ServerRequest) = ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(service.findCustomerById(request.pathVariable("id").toLong()), Customer::class.java)
            .switchIfEmpty(ServerResponse.notFound().build())

    fun update(request: ServerRequest) = request
            .bodyToMono(Customer::class.java)
            .zipWith(this.service.findCustomerById(request.pathVariable("id").toLong())) { item, existingItem ->
                Customer(existingItem.id, item.firstname, item.lastname)
            }
            .flatMap(::saveAndRespond)
            .switchIfEmpty(ServerResponse.notFound().build())

    fun addItem(request: ServerRequest) = request
            .bodyToMono(Customer::class.java)
            .flatMap(::saveAndRespond)

    private fun saveAndRespond(item: Customer) = ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(service. createCustomer(item), Customer::class.java)
}