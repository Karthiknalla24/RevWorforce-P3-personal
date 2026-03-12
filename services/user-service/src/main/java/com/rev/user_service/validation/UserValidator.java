package com.rev.user_service.validation;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateCreateUser(CreateUserRequest request) {

        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new BadRequestException("First name is required");
        }

        if (request.getLastName() == null || request.getLastName().isBlank()) {
            throw new BadRequestException("Last name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }

        if (request.getEmployeeId() == null || request.getEmployeeId().isBlank()) {
            throw new BadRequestException("Employee ID is required");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }
    }
}