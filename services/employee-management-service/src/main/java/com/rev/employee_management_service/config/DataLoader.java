package com.rev.employee_management_service.config;

import com.rev.employee_management_service.entity.Department;
import com.rev.employee_management_service.entity.Designation;
import com.rev.employee_management_service.entity.Announcement;
import com.rev.employee_management_service.repository.DepartmentRepository;
import com.rev.employee_management_service.repository.DesignationRepository;
import com.rev.employee_management_service.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final AnnouncementRepository announcementRepository;

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0) {
            createDepartment("HR", "Human Resources Department");
            createDepartment("Engineering", "Software Development and Engineering");
            createDepartment("Finance", "Financial Planning and Accounting");
            createDepartment("Marketing", "Marketing and Sales");
            createDepartment("Operations", "Business Operations");
        }

        if (designationRepository.count() == 0) {
            createDesignation("Manager", "Manages team members");
            createDesignation("Software Engineer", "Handles development tasks");
            createDesignation("Senior Developer", "Senior software developer");
            createDesignation("HR Executive", "Human resources executive");
            createDesignation("Finance Analyst", "Financial analysis and reporting");
            createDesignation("Marketing Executive", "Marketing and promotions");
            createDesignation("Operations Manager", "Manages business operations");
        }

        if (announcementRepository.count() == 0) {
            Announcement ann1 = new Announcement();
            ann1.setTitle("Welcome to RevWorkforce Platform");
            ann1.setContent("We are excited to launch the new RevWorkforce platform. This platform will help us manage our workforce more efficiently. Please explore the features and provide feedback.");
            announcementRepository.save(ann1);

            Announcement ann2 = new Announcement();
            ann2.setTitle("Company Holiday Schedule 2026");
            ann2.setContent("Please note the company holidays for 2026: New Year (Jan 1), Republic Day (Jan 26), Independence Day (Aug 15), Gandhi Jayanti (Oct 2), Christmas (Dec 25).");
            announcementRepository.save(ann2);
        }
    }

    private void createDepartment(String name, String description) {
        Department dept = new Department();
        dept.setName(name);
        dept.setDescription(description);
        departmentRepository.save(dept);
    }

    private void createDesignation(String title, String description) {
        Designation d = new Designation();
        d.setTitle(title);
        d.setDescription(description);
        designationRepository.save(d);
    }
}
