package com.rev.reporting_service.repository;

import com.rev.reporting_service.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByGeneratedBy(Long userId);
    List<Report> findByType(String type);
}
