package com.rev.employee_management_service.mapper;

import com.rev.employee_management_service.dto.response.EmployeeResponse;
import com.rev.employee_management_service.entity.Employee;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public Map<String, Object> toEmployeeMap(Employee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", employee.getId());
        map.put("firstName", employee.getFirstName());
        map.put("lastName", employee.getLastName());
        map.put("email", employee.getEmail());
        if (employee.getDepartment() != null) {
            map.put("departmentName", employee.getDepartment().getName());
        }
        return map;
    }

    public EmployeeResponse toEmployeeResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        if (employee.getDepartment() != null) {
            response.setDepartmentId(employee.getDepartment().getId());
            response.setDepartmentName(employee.getDepartment().getName());
        }
        if (employee.getDesignation() != null) {
            response.setDesignationId(employee.getDesignation().getId());
            response.setDesignationName(employee.getDesignation().getTitle());
        }
        return response;
    }

    public List<EmployeeResponse> toEmployeeResponseList(List<Employee> employees) {
        return employees.stream()
                .map(this::toEmployeeResponse)
                .collect(Collectors.toList());
    }
}
