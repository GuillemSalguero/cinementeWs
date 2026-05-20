package com.app.auth.controller;

import com.app.auth.dto.DirectorLikeRequest;
import com.app.auth.dto.FavoriteDirectorsResponse;
import com.app.auth.entity.Movie;
import com.app.auth.entity.User;
import com.app.auth.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    // GET /api/directors/{name}/movies
    @GetMapping("/{name}/movies")
    public ResponseEntity<List<Movie>> getMoviesByDirector(@PathVariable String name) {
        return ResponseEntity.ok(directorService.getMoviesByDirector(name));
    }

    // POST /api/directors/like
    @PostMapping("/like")
    public ResponseEntity<Void> likeDirector(@RequestBody DirectorLikeRequest request) {
        directorService.addFavoriteDirector(request.directorName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DELETE /api/directors/like
    @DeleteMapping("/like")
    public ResponseEntity<Void> unlikeDirector(@RequestBody DirectorLikeRequest request) {
        directorService.removeFavoriteDirector(request.directorName());
        return ResponseEntity.noContent().build();
    }

    // GET /api/directors/favorites
    @GetMapping("/favorites")
    public ResponseEntity<FavoriteDirectorsResponse> getFavorites(
            @AuthenticationPrincipal User user) {
        List<String> directors = directorService.getFavoriteDirectors();
        return ResponseEntity.ok(new FavoriteDirectorsResponse(user.getId(), directors));
    }
}