package com.rev.leave_service.repository;

import com.rev.leave_service.entity.Leave;
import com.rev.leave_service.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUserId(Long userId);
    List<Leave> findByManagerId(Long managerId);
    List<Leave> findByUserIdAndStatus(Long userId, LeaveStatus status);

    @Query("SELECT l FROM Leave l WHERE l.userId = :userId " +
           "AND l.status NOT IN ('REJECTED', 'CANCELLED') " +
           "AND l.startDate <= :endDate AND l.endDate >= :startDate")
    List<Leave> findOverlappingLeaves(@Param("userId") Long userId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
