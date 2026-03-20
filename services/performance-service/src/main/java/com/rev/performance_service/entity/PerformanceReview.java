package com.rev.performance_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "performance_reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "year"})
})
@Getter
@Setter
@NoArgsConstructor
public class PerformanceReview extends BaseEntity {

    @Transient
    private String employeeName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int year;

    private String deliverables;
    @com.fasterxml.jackson.annotation.JsonProperty("areasOfAccomplishment")
    private String accomplishments;
    @com.fasterxml.jackson.annotation.JsonProperty("areasOfImprovement")
    private String improvements;

    private int selfRating;
    private int managerRating;

    @com.fasterxml.jackson.annotation.JsonProperty("feedback")
    private String managerFeedback;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.DRAFT;

    // Use property based getters to ensure both original and aliased names are in the JSON output
    @com.fasterxml.jackson.annotation.JsonProperty("accomplishments")
    public String getAccomplishmentsLegacy() { return accomplishments; }

    @com.fasterxml.jackson.annotation.JsonProperty("improvements")
    public String getImprovementsLegacy() { return improvements; }

    @com.fasterxml.jackson.annotation.JsonProperty("areasOfAccomplishments")
    public String getAreasOfAccomplishmentsPlural() { return accomplishments; }

    @com.fasterxml.jackson.annotation.JsonProperty("areasOfImprovements")
    public String getAreasOfImprovementsPlural() { return improvements; }

    @com.fasterxml.jackson.annotation.JsonProperty("improvementAreas")
    public String getImprovementAreas() { return improvements; }
}
