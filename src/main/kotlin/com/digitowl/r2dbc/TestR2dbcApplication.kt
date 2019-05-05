package com.digitowl.r2dbc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TestR2dbcApplication

fun main(args: Array<String>) {
    SpringApplication.run(TestR2dbcApplication::class.java, *args)
}