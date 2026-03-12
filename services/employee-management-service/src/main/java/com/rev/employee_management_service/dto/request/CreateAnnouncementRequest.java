package com.rev.employee_management_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnnouncementRequest {
    private String title;
    private String content;
}
