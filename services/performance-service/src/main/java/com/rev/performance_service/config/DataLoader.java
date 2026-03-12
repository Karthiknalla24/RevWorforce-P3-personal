package com.rev.performance_service.config;

import com.rev.performance_service.entity.*;
import com.rev.performance_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ReviewRepository reviewRepository;
    private final GoalRepository goalRepository;

    @Override
    public void run(String... args) throws Exception {
        if (reviewRepository.count() == 0) {
            // Vivek Murugan(3)
            PerformanceReview review1 = new PerformanceReview();
            review1.setUserId(3L);
            review1.setYear(2026);
            review1.setDeliverables("Completed P3 Microservices Migration");
            review1.setAccomplishments("Successfully split the monolith into 9 services");
            review1.setImprovements("Need to focus more on integration testing");
            review1.setSelfRating(4);
            review1.setStatus(ReviewStatus.SUBMITTED);
            reviewRepository.save(review1);
        }

        if (goalRepository.count() == 0) {
            // Vivek Murugan(3)
            Goal goal1 = new Goal();
            goal1.setUserId(3L);
            goal1.setTitle("Optimize Service Discovery");
            goal1.setDescription("Improve Eureka registration and discovery performance");
            goal1.setProgress(50);
            goal1.setStatus(GoalStatus.IN_PROGRESS);
            goalRepository.save(goal1);
            
            // Chaithanya (2)
            Goal goal2 = new Goal();
            goal2.setUserId(2L);
            goal2.setTitle("Team Training");
            goal2.setDescription("Train the team on the new microservices architecture");
            goal2.setProgress(30);
            goal2.setStatus(GoalStatus.IN_PROGRESS);
            goalRepository.save(goal2);
        }
    }
}
