package com.app.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_movie_reviews", schema = "public")
@IdClass(ReviewId.class)
@Data
public class Review {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "movie_link", nullable = false)
    private String movieLink;

    // Tu SQL dice numeric(3, 1), por lo que admite decimales (ej. 8.5). 
    // Usamos Double en lugar de Integer.
    @Column(nullable = false)
    private Double rating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    // Tu SQL usa "timestamp with time zone", OffsetDateTime es el mapeo correcto en Java
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}