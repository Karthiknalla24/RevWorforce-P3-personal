package com.rev.reporting_service.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.rev.reporting_service.client")
public class FeignConfig {
    // Only handles Feign client enabling
}
