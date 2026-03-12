package com.rev.employee_management_service.repository;

import com.rev.employee_management_service.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
    Optional<Designation> findByTitle(String title);
    boolean existsByTitle(String title);
}
