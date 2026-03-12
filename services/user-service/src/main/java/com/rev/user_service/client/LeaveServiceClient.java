package com.rev.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "leave-service")
public interface LeaveServiceClient {

    @PostMapping("/api/leaves/balance")
    void assignBalance(@RequestBody Map<String, Object> request);
}
