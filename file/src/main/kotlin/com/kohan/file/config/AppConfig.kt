package com.kohan.file.config

import com.kohan.file.service.grpc.DownloadFileGrpcService
import com.kohan.file.service.grpc.UploadFileGrpcService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class AppConfig {
    @Bean
    fun armeriaServerConfigurator(
        uploadFileGrpcService: UploadFileGrpcService,
        downloadFileGrpcService: DownloadFileGrpcService,
    ): ArmeriaServerConfigurator =
        ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.service(
                "prefix:/grpc/v1",
                GrpcService
                    .builder()
                    .addService(uploadFileGrpcService)
                    .addService(downloadFileGrpcService)
                    .enableUnframedRequests(false)
                    .useClientTimeoutHeader(false)
                    .build(),
            )
            serverBuilder.serviceUnder("/docs", DocService())
            serverBuilder.decorator(LoggingService.newDecorator())
            serverBuilder.accessLogWriter(AccessLogWriter.combined(), false)
            serverBuilder.requestTimeout(Duration.ofHours(1))
            serverBuilder.maxRequestLength(10L * 1024L * 1024L * 1024L * Long.SIZE_BYTES) // 10GB
        }
}
