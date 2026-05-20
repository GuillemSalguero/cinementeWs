package com.app.auth.service;

import com.app.auth.dto.ReviewResponse;
import com.app.auth.entity.Movie;
import com.app.auth.repository.MovieRepository;
import com.app.auth.entity.Review;
import com.app.auth.dto.MovieDtos2.*;
import com.app.auth.repository.MovieSpecification;
import com.app.auth.repository.ReviewRepository;
import com.app.auth.repository.UserFavoriteMovieRepository;


import java.util.List;

import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieService2 {

    private final MovieRepository movieRepository;
    private final UserFavoriteMovieRepository userFavoriteMovieRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    public MovieService2(MovieRepository movieRepository, UserFavoriteMovieRepository userFavoriteMovieRepository) {
        this.movieRepository = movieRepository;
        this.userFavoriteMovieRepository = userFavoriteMovieRepository;
    }

    public Page<MovieSummaryResponse> search(
            String q,
            String genre, String director, String actor,
            String contentRating, String tomatometerStatus,
            Long tomatometerMin, Long tomatometerMax,
            Long audienceMin, Long audienceMax,
            String poster_url,
            int page, int size, String sortBy, String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Specification<Movie> spec = Specification
                .where(MovieSpecification.titleContains(q))
                .and(MovieSpecification.genreContains(genre))
                .and(MovieSpecification.directorContains(director))
                .and(MovieSpecification.actorContains(actor))
                .and(MovieSpecification.contentRatingEquals(contentRating))
                .and(MovieSpecification.tomatometerStatusEquals(tomatometerStatus))
                .and(MovieSpecification.tomatometerRatingBetween(tomatometerMin, tomatometerMax))
                .and(MovieSpecification.audienceRatingBetween(audienceMin, audienceMax));

        return movieRepository.findAll(spec, PageRequest.of(page, size, sort))
                .map(MovieSummaryResponse::from);
    }

    public MovieDetailResponse getById(String rottenTomatoesLink) {
        Movie movie = movieRepository.findById(rottenTomatoesLink)
                .orElseThrow(() -> new IllegalArgumentException("Película no encontrada: " + rottenTomatoesLink));
        return MovieDetailResponse.from(movie);
    }
    public List<ReviewResponse> getPublicReviews(String movieLink) {
        // Buscamos las 12 últimas reviews en la base de datos
        List<Review> reviews = reviewRepository.findTop12ByMovieLinkOrderByCreatedAtDesc(movieLink);

        // Convertimos cada entidad Review al DTO ReviewResponse que me pediste
        return reviews.stream().map(review -> {
            ReviewResponse response = new ReviewResponse();
            response.setUserId(review.getUserId());
            response.setMovieLink(review.getMovieLink());
            response.setRating(review.getRating());
            response.setReviewText(review.getReviewText());
            response.setCreatedAt(review.getCreatedAt());
            return response;
        }).collect(Collectors.toList());
    }

    public List<MovieSummaryResponse> getFavList(Long userId) {
        return userFavoriteMovieRepository
            .findTop12ByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(ufm -> toSummary(ufm.getMovie()))
            .collect(Collectors.toList());
    }

    public List<MovieSummaryResponse> getFriendsFavList(Long userId) {
        return userFavoriteMovieRepository
            .findTop12FromFriendsNoRepeat(userId)
            .stream()
            .map(ufm -> toSummary(ufm.getMovie()))
            .distinct()
            .collect(Collectors.toList());
    }

    private MovieSummaryResponse toSummary(Movie m) {
        return MovieSummaryResponse.from(m);
    }
}