package com.rev.reporting_service.mapper;

import com.rev.reporting_service.entity.Report;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReportMapper {
    public Map<String, Object> toResponseMap(Report report) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", report.getId());
        map.put("name", report.getName());
        map.put("type", report.getType());
        map.put("generatedBy", report.getGeneratedBy());
        map.put("createdAt", report.getCreatedAt());
        return map;
    }
}
