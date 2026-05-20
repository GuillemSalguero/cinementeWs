package com.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import java.util.List;

public class UserDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String name;
        private String favFilms;
        private Long friend;
        private OffsetDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendResponse {
        private Long friendId;
        private String friendName;
        private OffsetDateTime since;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendMoviesResponse {
        private Long friendId;
        private String friendName;
        private List<String> favoriteMovies;   // lista de movieLinks
        private List<String> watchlist;        // lista de movieLinks
    }
}