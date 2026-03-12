package com.rev.reporting_service.dto.request;

import lombok.Data;

@Data
public class GenerateReportRequest {
    private String type;
    private Long id; // userId or departmentId
    private String name;
}
