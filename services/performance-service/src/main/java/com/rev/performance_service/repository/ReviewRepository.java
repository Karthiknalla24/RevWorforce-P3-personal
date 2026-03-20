package com.rev.performance_service.repository;

import com.rev.performance_service.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findByUserId(Long userId);
    List<PerformanceReview> findByYear(int year);
    boolean existsByUserIdAndYear(Long userId, int year);
}
