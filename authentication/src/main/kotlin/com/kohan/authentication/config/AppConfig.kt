package com.kohan.authentication.config

import com.kohan.authentication.service.annotation.UserService
import com.kohan.authentication.service.grpc.AuthenticationGrpcService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun armeriaServerConfigurator(
        userService: UserService,
        authenticationGrpcService: AuthenticationGrpcService,
    ): ArmeriaServerConfigurator =
        ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.annotatedService("/api", userService)
            serverBuilder.service(
                "prefix:/grpc/v1",
                GrpcService
                    .builder()
                    .addService(authenticationGrpcService)
                    .enableUnframedRequests(true)
                    .build(),
            )

            serverBuilder.serviceUnder("/docs", DocService())
            serverBuilder.decorator(LoggingService.newDecorator())
            serverBuilder.accessLogWriter(AccessLogWriter.combined(), false)
        }
}
