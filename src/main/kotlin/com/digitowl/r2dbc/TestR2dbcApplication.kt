package com.digitowl.r2dbc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class TestR2dbcApplication

fun main(args: Array<String>) {
    SpringApplication.run(TestR2dbcApplication::class.java, *args)
}