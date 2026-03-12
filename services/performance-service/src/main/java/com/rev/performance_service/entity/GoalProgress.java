package com.rev.performance_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goal_progress")
public class GoalProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;
    
    private Integer progressPercentage;
    private String updateNote;
    private LocalDateTime updatedAt = LocalDateTime.now();
}
