package com.rev.employee_management_service.repository;

import com.rev.employee_management_service.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
