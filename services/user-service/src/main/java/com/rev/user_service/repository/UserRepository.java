package com.rev.user_service.repository;

import com.rev.user_service.entity.Role;
import com.rev.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    Optional<User> findByEmployeeIdOrEmail(String employeeId, String email);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    List<User> findByManagerId(Long managerId);

    List<User> findByRole(Role role);

    long countByDepartmentId(Long departmentId);

    long countByDesignationId(Long designationId);

}