package com.rev.reporting_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", contextId = "userClient")
public interface UserClient {
    
    @GetMapping("/api/users/{id}")
    Object getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/directory")
    List<Map<String, Object>> getEmployeeDirectory();

    @GetMapping("/api/users/directory")
    Object getAllUsers();
}
