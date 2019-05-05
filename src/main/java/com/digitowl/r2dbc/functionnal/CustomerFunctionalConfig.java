package com.digitowl.r2dbc.functionnal;

import com.digitowl.r2dbc.domain.Customer;
import com.digitowl.r2dbc.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Configuration
public class CustomerFunctionalConfig {

    @Autowired
    CustomerService customerService;

    @Bean
    RouterFunction<ServerResponse> getCustomerByIdRoute() {
        return route(GET("/customers/{id}"),
                req -> ok().body(
                        customerService.findCustomerById(Long.parseLong(req.pathVariable("id"))), Customer.class));
    }

    @Bean
    RouterFunction<ServerResponse> getAllEmployeesRoute() {
        return route(GET("/customers"),
                req -> ok().body(
                        customerService.findAll(), Customer.class));
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf()
                .disable()
                .authorizeExchange()
                .anyExchange()
                .permitAll();
        return http.build();
    }
}
