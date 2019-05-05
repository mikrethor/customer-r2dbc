package com.digitowl.r2dbc.functionnal

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

fun router(customerHandler: CustomerHandler) = router {
    accept(APPLICATION_JSON).nest {
        "/api".nest {
            "/customers".nest {
                GET("/", customerHandler::findAll)
            }
            "/customers".nest {
                PUT("/{id}", customerHandler::update)
                GET("/{id}", customerHandler::findCustomerById)
                POST("/add", customerHandler::addItem)
            }
        }
    }
}