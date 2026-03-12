package com.rev.user_service.util;

import com.rev.user_service.entity.Role;
import com.rev.user_service.entity.User;
import com.rev.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HierarchySetupRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting safe hierarchy setup via entities...");

        // 1. Find the Admin
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        if (admins.isEmpty()) {
            log.warn("No ADMIN user found. Skipping manager-to-admin link setup.");
            return;
        }
        
        User admin = admins.get(0);
        log.info("Found system administrator: {} (ID: {})", admin.getEmail(), admin.getId());

        // 2. Find all Managers
        List<User> managers = userRepository.findByRole(Role.MANAGER);
        int updatedCount = 0;

        for (User manager : managers) {
            // Only update if they don't have a manager assigned, or if they are assigned to someone else
            // In your project, every Manager should report to the Admin.
            if (manager.getManagerId() == null || !manager.getManagerId().equals(admin.getId())) {
                manager.setManagerId(admin.getId());
                userRepository.save(manager);
                updatedCount++;
            }
        }

        if (updatedCount > 0) {
            log.info("Hierarchy formalized: Linked {} Managers to the Admin via JPA entities.", updatedCount);
        } else {
            log.info("Safe hierarchy is already correct. No updates needed.");
        }
    }
}
