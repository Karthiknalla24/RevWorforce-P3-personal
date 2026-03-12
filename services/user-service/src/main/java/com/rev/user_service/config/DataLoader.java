package com.rev.user_service.config;

import com.rev.user_service.entity.User;
import com.rev.user_service.entity.Role;
import com.rev.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            
            // Karthik Nalla - ADMIN
            User karthik = new User();
            karthik.setEmployeeId("EMP001");
            karthik.setEmail("karthik@revworkforce.com");
            karthik.setFirstName("Karthik");
            karthik.setLastName("Nalla");
            karthik.setPassword(passwordEncoder.encode("admin123"));
            karthik.setPhone("9876543210");
            karthik.setDepartment("HR");
            karthik.setDesignation("Admin");
            karthik.setDepartmentId(1L);
            karthik.setDesignationId(1L);
            karthik.setRole(Role.ADMIN);
            karthik.setActive(true);
            karthik.setJoiningDate(LocalDate.now());
            userRepository.save(karthik);

            // Chaithanya Palamani - MANAGER
            User chaithanya = new User();
            chaithanya.setEmployeeId("EMP002");
            chaithanya.setEmail("chaithanya@revworkforce.com");
            chaithanya.setFirstName("Chaithanya");
            chaithanya.setLastName("Palamani");
            chaithanya.setPassword(passwordEncoder.encode("admin123"));
            chaithanya.setPhone("9876543211");
            chaithanya.setDepartment("HR");
            chaithanya.setDesignation("Manager");
            chaithanya.setDepartmentId(1L);
            chaithanya.setDesignationId(1L);
            chaithanya.setRole(Role.MANAGER);
            chaithanya.setActive(true);
            chaithanya.setJoiningDate(LocalDate.now());
            userRepository.save(chaithanya);

            // Vivek Murugan- EMPLOYEE
            User Vivek = new User();
            Vivek.setEmployeeId("EMP003");
            Vivek.setEmail("vivek@revworkforce.com");
            Vivek.setFirstName("Vivek");
            Vivek.setLastName("Murugan");
            Vivek.setPassword(passwordEncoder.encode("admin123"));
            Vivek.setPhone("9876543212");
            Vivek.setDepartment("HR");
            Vivek.setDesignation("Software Engineer");
            Vivek.setDepartmentId(1L);
            Vivek.setDesignationId(2L);
            Vivek.setManagerId(2L); // Managed by Chaithanya
            Vivek.setRole(Role.EMPLOYEE);
            Vivek.setActive(true);
            Vivek.setJoiningDate(LocalDate.now());
            userRepository.save(Vivek);
        } else {
            // Ensure live DB data is updated if already seeded
            userRepository.findByEmail("karthik@revworkforce.com").ifPresent(k -> {
                if (!"Admin".equals(k.getDesignation())) {
                    k.setDesignation("Admin");
                    k.setRole(Role.ADMIN);
                    userRepository.save(k);
                }
            });
        }
    }
}
