package com.app.auth.dto;

import com.app.auth.entity.Movie;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class MovieDtos2 {

    @Data
    public static class MovieIdRequest {
        @NotBlank(message = "El id es obligatorio")
        private String id;
    }

    @Data
    public static class MovieDetailResponse {
        private String rottenTomatoesLink;
        private String movieTitle;
        private String movieInfo;
        private String criticsConsensus;
        private String contentRating;
        private String genres;
        private String directors;
        private String authors;
        private String actors;
        private String originalReleaseDate;
        private String streamingReleaseDate;
        private Long   runtime;
        private String productionCompany;
        private String tomatometerStatus;
        private Long   tomatometerRating;
        private Long   tomatometerCount;
        private String audienceStatus;
        private Long   audienceRating;
        private Long   audienceCount;
        private Long   tomatometerFreshCriticsCount;
        private String tmdbId;
        private String posterUrl;

        public static MovieDetailResponse from(Movie m) {
            MovieDetailResponse r = new MovieDetailResponse();
            r.rottenTomatoesLink           = m.getRottenTomatoesLink();
            r.movieTitle                   = m.getMovieTitle();
            r.movieInfo                    = m.getMovieInfo();
            r.criticsConsensus             = m.getCriticsConsensus();
            r.contentRating                = m.getContentRating();
            r.genres                       = m.getGenres();
            r.directors                    = m.getDirectors();
            r.authors                      = m.getAuthors();
            r.actors                       = m.getActors();
            r.originalReleaseDate          = m.getOriginalReleaseDate();
            r.streamingReleaseDate         = m.getStreamingReleaseDate();
            r.runtime                      = m.getRuntime();
            r.productionCompany            = m.getProductionCompany();
            r.tomatometerStatus            = m.getTomatometerStatus();
            r.tomatometerRating            = m.getTomatometerRating();
            r.tomatometerCount             = m.getTomatometerCount();
            r.audienceStatus               = m.getAudienceStatus();
            r.audienceRating               = m.getAudienceRating();
            r.audienceCount                = m.getAudienceCount();
            r.tomatometerFreshCriticsCount = m.getTomatometerFreshCriticsCount();
            r.tmdbId                       = m.getTmdbId();
            r.posterUrl                    = m.getPoster_url();
            return r;
        }
    }

    @Data
    public static class MovieSummaryResponse {
        private String rottenTomatoesLink;
        private String movieTitle;
        private String genres;
        private String contentRating;
        private String directors;
        private Long   runtime;
        private Long   tomatometerRating;
        private String tomatometerStatus;
        private Long   audienceRating;
        private String audienceStatus;
        private String posterUrl;
        private String originalReleaseDate;

        public static MovieSummaryResponse from(Movie m) {
            MovieSummaryResponse r = new MovieSummaryResponse();
            r.rottenTomatoesLink = m.getRottenTomatoesLink();
            r.movieTitle         = m.getMovieTitle();
            r.genres             = m.getGenres();
            r.contentRating      = m.getContentRating();
            r.directors          = m.getDirectors();
            r.runtime            = m.getRuntime();
            r.tomatometerRating  = m.getTomatometerRating();
            r.tomatometerStatus  = m.getTomatometerStatus();
            r.audienceRating     = m.getAudienceRating();
            r.audienceStatus     = m.getAudienceStatus();
            r.originalReleaseDate = m.getOriginalReleaseDate();
            r.posterUrl = m.getPoster_url();
            return r;
        }
    }
}