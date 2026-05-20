package com.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long userId;
    private String movieLink;
    private Double rating; // Cambiado a Double
    private String reviewText;
    private OffsetDateTime createdAt; // Cambiado a OffsetDateTime
}