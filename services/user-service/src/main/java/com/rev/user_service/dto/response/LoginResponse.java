package com.rev.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;

    private Long userId;

    private String employeeId;

    private String email;

    private String name;

    private String firstName;

    private String lastName;

    private String role;

    private String designation;

    private String department;

}