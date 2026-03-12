package com.rev.performance_service.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.rev.performance_service.client")
public class FeignConfig {
    // Only handles Feign client enabling
}
