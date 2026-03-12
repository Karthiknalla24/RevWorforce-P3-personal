package com.rev.leave_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", contextId = "userServiceClient")
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    Map<String, Object> getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/{id}/manager")
    Map<String, Object> getManager(@PathVariable("id") Long id);

    @GetMapping("/api/users/manager/{managerId}/team")
    List<Map<String, Object>> getTeamMembers(@PathVariable("managerId") Long managerId);

    @GetMapping("/api/users/directory")
    List<Map<String, Object>> getEmployeeDirectory();
}
