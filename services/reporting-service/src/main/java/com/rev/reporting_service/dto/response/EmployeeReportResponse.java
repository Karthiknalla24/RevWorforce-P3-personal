package com.rev.reporting_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReportResponse {
    private Object user;
    private Object leaves;
    private Object reviews;
    private Object goals;
    private Object leaveBalance;
    private long generatedAt;
}
