package com.app.auth.controller;

import com.app.auth.dto.MovieDtos.*;
import com.app.auth.service.MovieService;
import com.app.auth.service.MovieService2;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Todos los endpoints requieren: Authorization: Bearer <token>
 *
 * FAVORITOS:
 *   GET    /api/movies/favorites          → mis favoritos
 *   POST   /api/movies/favorites          → añadir favorito
 *   DELETE /api/movies/favorites          → quitar favorito
 *
 * WATCHLIST:
 *   GET    /api/movies/watchlist          → mi watchlist
 *   POST   /api/movies/watchlist          → añadir al watchlist
 *   DELETE /api/movies/watchlist          → quitar del watchlist
 *
 * REVIEWS:
 *   GET    /api/movies/reviews            → mis reviews
 *   POST   /api/movies/reviews            → crear/actualizar review
 *   DELETE /api/movies/reviews            → borrar review
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    @Autowired
    private MovieService2 movieService2;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // ─── Favoritos ────────────────────────────────────────────────

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites() {
        return ResponseEntity.ok(movieService.getMyFavorites());
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @Valid @RequestBody MovieLinkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieService.addFavorite(request.getMovieLink()));
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<MessageResponse> removeFavorite(
            @Valid @RequestBody MovieLinkRequest request) {
        movieService.removeFavorite(request.getMovieLink());
        return ResponseEntity.ok(new MessageResponse("Película eliminada de favoritos"));
    }

    // ─── Watchlist ────────────────────────────────────────────────

    @GetMapping("/watchlist")
    public ResponseEntity<List<WatchlistResponse>> getMyWatchlist() {
        return ResponseEntity.ok(movieService.getMyWatchlist());
    }

    @PostMapping("/watchlist")
    public ResponseEntity<WatchlistResponse> addToWatchlist(
            @Valid @RequestBody MovieLinkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieService.addToWatchlist(request.getMovieLink()));
    }

    @DeleteMapping("/watchlist")
    public ResponseEntity<MessageResponse> removeFromWatchlist(
            @Valid @RequestBody MovieLinkRequest request) {
        movieService.removeFromWatchlist(request.getMovieLink());
        return ResponseEntity.ok(new MessageResponse("Película eliminada del watchlist"));
    }

    // ─── Reviews ──────────────────────────────────────────────────

    @PostMapping("/SearchReviews")
    public ResponseEntity<List<ReviewResponse>> getPublicReviews(
            @Valid @RequestBody MovieLinkRequest request) {
        return ResponseEntity.ok(movieService.getPublicReviews(request.getMovieLink()));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> upsertReview(
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(movieService.upsertReview(request));
    }

    @DeleteMapping("/reviews")
    public ResponseEntity<MessageResponse> deleteReview(
            @Valid @RequestBody MovieLinkRequest request) {
        movieService.deleteReview(request.getMovieLink());
        return ResponseEntity.ok(new MessageResponse("Review eliminada"));
    }

    /*@PostMapping("/reviews/public")
    public ResponseEntity<List<ReviewResponse>> getPublicReviews(
            // Change to RequestBody and use a Map or a custom DTO class
            @RequestBody Map<String, String> payload) {
        
        String movieLink = payload.get("movieLink");
        return ResponseEntity.ok(movieService.getPublicReviews(movieLink));
    }*/
}
