package com.rev.reporting_service.repository;

import com.rev.reporting_service.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    org.springframework.data.domain.Page<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<ActivityLog> findAllByOrderByCreatedAtDesc(org.springframework.data.domain.Pageable pageable);
    
    @org.springframework.transaction.annotation.Transactional
    void deleteByCreatedAtBefore(java.time.LocalDateTime date);
}
