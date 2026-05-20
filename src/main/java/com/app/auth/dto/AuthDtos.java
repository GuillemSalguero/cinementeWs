package com.app.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// ─── Request DTOs ─────────────────────────────────────────────────────────────

public class AuthDtos {

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        private String name;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private String favFilms;

        private Long friend;          // optional: ID of a friend (validated by DB trigger)
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Password is required")
        private String password;
    }

    // ─── Response DTOs ────────────────────────────────────────────────────────

    @Data
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private long expiresIn;          // seconds
        private UserInfo user;

        public AuthResponse(String accessToken, String refreshToken, long expiresIn, UserInfo user) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn   = expiresIn;
            this.user        = user;
        }
    }

    @Data
    public static class UserInfo {
        private Long   id;
        private String name;
        private String favFilms;
        private Long   friend;

        public UserInfo(Long id, String name, String favFilms, Long friend) {
            this.id       = id;
            this.name     = name;
            this.favFilms = favFilms;
            this.friend   = friend;
        }
    }

    @Data
    public static class ApiError {
        private int    status;
        private String error;
        private String message;

        public ApiError(int status, String error, String message) {
            this.status  = status;
            this.error   = error;
            this.message = message;
        }
    }
}
