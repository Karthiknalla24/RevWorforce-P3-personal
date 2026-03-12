package com.rev.user_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchUserRequest {

    private String name;

    private String employeeId;

    private String department;

    private String designation;

}