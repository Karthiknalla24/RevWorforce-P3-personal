package com.rev.leave_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeaveTypeRequest {
    private String name;
    private Integer defaultQuota;
    private Integer maxDaysPerYear; // alias
}
