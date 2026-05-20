package com.app.auth.controller;

import com.app.auth.dto.MovieDtos2.MovieDetailResponse;
import com.app.auth.dto.MovieDtos2.MovieIdRequest;
import com.app.auth.dto.MovieDtos2.MovieSummaryResponse;
import com.app.auth.service.MovieService2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.auth.dto.ReviewResponse;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController2 {

    private final MovieService2 movieService;

    public MovieController2(MovieService2 movieService) {
        this.movieService = movieService;
    }

    // GET /api/movies?q=inception&page=0&size=20
    @GetMapping
    public ResponseEntity<Page<MovieSummaryResponse>> search(
            @RequestParam(required = false)    String q,
            @RequestParam(required = false)    String genre,
            @RequestParam(required = false)    String director,
            @RequestParam(required = false)    String actor,
            @RequestParam(required = false)    String contentRating,
            @RequestParam(required = false)    String tomatometerStatus,
            @RequestParam(required = false)    Long   tomatometerMin,
            @RequestParam(required = false)    Long   tomatometerMax,
            @RequestParam(required = false)    Long   audienceMin,
            @RequestParam(required = false)    Long   audienceMax,
            @RequestParam(required = false)    String   poster_url,
            @RequestParam(defaultValue = "0")  int    page,
            @RequestParam(defaultValue = "20") int    size,
            @RequestParam(defaultValue = "tomatometerRating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(movieService.search(
                q, genre, director, actor,
                contentRating, tomatometerStatus,
                tomatometerMin, tomatometerMax,
                audienceMin, audienceMax, poster_url,
                page, size, sortBy, sortDir
        ));
    }

    // POST /api/movies/detail
    // Body: { "id": "/m/inception" }
    @PostMapping("/detail")
    public ResponseEntity<MovieDetailResponse> getDetail(@RequestBody MovieIdRequest request) {
        return ResponseEntity.ok(movieService.getById(request.getId()));
    }

    
    @PostMapping("/reviews/public")
    public ResponseEntity<List<ReviewResponse>> getPublicReviews(
            @RequestBody Map<String, String> payload) {
        String movieLink = payload.get("movieLink");
        return ResponseEntity.ok(movieService.getPublicReviews(movieLink));
    }

    @GetMapping("/favlist")
    public ResponseEntity<List<MovieSummaryResponse>> getFavList(
            @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getFavList(userId));
    }

    @GetMapping("/friends-favlist")
    public ResponseEntity<List<MovieSummaryResponse>> getFriendsFavList(
            @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getFriendsFavList(userId));
    }
}