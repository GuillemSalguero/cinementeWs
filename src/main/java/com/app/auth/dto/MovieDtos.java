package com.app.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class MovieDtos {

    // ─── Requests ──────────────────────────────────────────────────

    @Data
    public static class MovieLinkRequest {
        @NotBlank(message = "El enlace de la película es obligatorio")
        private String movieLink;
    }

    @Data
    public static class ReviewRequest {
        @NotBlank(message = "El enlace de la película es obligatorio")
        private String movieLink;

        @NotNull(message = "El rating es obligatorio")
        @DecimalMin(value = "0.0", message = "El rating mínimo es 0.0")
        @DecimalMax(value = "10.0", message = "El rating máximo es 10.0")
        private BigDecimal rating;

        private String reviewText;
    }

    // ─── Responses ─────────────────────────────────────────────────

    @Data
    public static class FavoriteResponse {
        private Long userId;
        private String movieLink;
        private OffsetDateTime createdAt;

        public FavoriteResponse(Long userId, String movieLink, OffsetDateTime createdAt) {
            this.userId    = userId;
            this.movieLink = movieLink;
            this.createdAt = createdAt;
        }
    }

    @Data
    public static class WatchlistResponse {
        private Long userId;
        private String movieLink;
        private OffsetDateTime createdAt;

        public WatchlistResponse(Long userId, String movieLink, OffsetDateTime createdAt) {
            this.userId    = userId;
            this.movieLink = movieLink;
            this.createdAt = createdAt;
        }
    }

    @Data
    public static class ReviewResponse {
        private Long userId;
        private String movieLink;
        private BigDecimal rating;
        private String reviewText;
        private OffsetDateTime createdAt;

        public ReviewResponse(Long userId, String movieLink, BigDecimal rating,
                              String reviewText, OffsetDateTime createdAt) {
            this.userId     = userId;
            this.movieLink  = movieLink;
            this.rating     = rating;
            this.reviewText = reviewText;
            this.createdAt  = createdAt;
        }
    }

    @Data
    public static class MessageResponse {
        private String message;
        public MessageResponse(String message) { this.message = message; }
    }
    
    public record FavoriteDirectorsResponse(
        Long userId,
        List<String> directors
    ) {}
}
