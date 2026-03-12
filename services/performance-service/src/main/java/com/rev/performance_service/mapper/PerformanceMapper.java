package com.rev.performance_service.mapper;

import com.rev.performance_service.entity.Goal;
import com.rev.performance_service.entity.PerformanceReview;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class PerformanceMapper {
    public Map<String, Object> toGoalMap(Goal goal) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", goal.getId());
        map.put("title", goal.getTitle());
        map.put("status", goal.getStatus());
        map.put("priority", goal.getPriority());
        return map;
    }

    public Map<String, Object> toReviewMap(PerformanceReview review) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", review.getId());
        map.put("userId", review.getUserId());
        map.put("year", review.getYear());
        map.put("deliverables", review.getDeliverables());
        map.put("areasOfAccomplishment", review.getAccomplishments());
        map.put("areasOfAccomplishments", review.getAccomplishments());
        map.put("accomplishments", review.getAccomplishments());
        map.put("areasOfImprovement", review.getImprovements());
        map.put("areasOfImprovements", review.getImprovements());
        map.put("improvements", review.getImprovements());
        map.put("improvementAreas", review.getImprovements());
        map.put("selfRating", review.getSelfRating());
        map.put("managerRating", review.getManagerRating());
        map.put("managerFeedback", review.getManagerFeedback());
        map.put("status", review.getStatus());
        return map;
    }
}
