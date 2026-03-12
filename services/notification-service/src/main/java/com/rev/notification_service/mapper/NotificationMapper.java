package com.rev.notification_service.mapper;

import com.rev.notification_service.dto.response.NotificationResponse;
import com.rev.notification_service.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .message(entity.getMessage())
                .type(entity.getType())
                .readStatus(entity.isReadStatus())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .build();
    }

    public List<NotificationResponse> toResponseList(List<Notification> list) {
        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
