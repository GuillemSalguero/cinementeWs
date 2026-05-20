package com.app.auth.repository;

import com.app.auth.entity.Movie;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    private MovieSpecification() {}

    public static Specification<Movie> titleContains(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank() ? cb.conjunction()
                : cb.like(cb.lower(root.get("movieTitle")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Movie> genreContains(String genre) {
        return (root, query, cb) ->
                genre == null || genre.isBlank() ? cb.conjunction()
                : cb.like(cb.lower(root.get("genres")), "%" + genre.toLowerCase() + "%");
    }

    public static Specification<Movie> directorContains(String director) {
        return (root, query, cb) ->
                director == null || director.isBlank() ? cb.conjunction()
                : cb.like(cb.lower(root.get("directors")), "%" + director.toLowerCase() + "%");
    }

    public static Specification<Movie> actorContains(String actor) {
        return (root, query, cb) ->
                actor == null || actor.isBlank() ? cb.conjunction()
                : cb.like(cb.lower(root.get("actors")), "%" + actor.toLowerCase() + "%");
    }

    public static Specification<Movie> contentRatingEquals(String contentRating) {
        return (root, query, cb) ->
                contentRating == null || contentRating.isBlank() ? cb.conjunction()
                : cb.equal(root.get("contentRating"), contentRating);
    }

    public static Specification<Movie> tomatometerRatingBetween(Long min, Long max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min == null) return cb.lessThanOrEqualTo(root.get("tomatometerRating"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("tomatometerRating"), min);
            return cb.between(root.get("tomatometerRating"), min, max);
        };
    }

    public static Specification<Movie> audienceRatingBetween(Long min, Long max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min == null) return cb.lessThanOrEqualTo(root.get("audienceRating"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("audienceRating"), min);
            return cb.between(root.get("audienceRating"), min, max);
        };
    }

    public static Specification<Movie> tomatometerStatusEquals(String status) {
        return (root, query, cb) ->
                status == null || status.isBlank() ? cb.conjunction()
                : cb.equal(root.get("tomatometerStatus"), status);
    }
}