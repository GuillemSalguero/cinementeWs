package com.app.auth.service;

import com.app.auth.entity.Movie;
import com.app.auth.entity.User;
import com.app.auth.entity.UserFavoriteDirector;
import com.app.auth.repository.MovieRepository;
import com.app.auth.repository.UserFavoriteDirectorRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DirectorService {

    private final MovieRepository movieRepository;
    private final UserFavoriteDirectorRepository favoriteRepository;

    public DirectorService(MovieRepository movieRepository,
                           UserFavoriteDirectorRepository favoriteRepository) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
    }

    // Obtener usuario autenticado desde Spring Security
    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // Películas de un director
    public List<Movie> getMoviesByDirector(String director) {
        return movieRepository.findByDirector(director);
    }

    // Dar like a un director
    public void addFavoriteDirector(String directorName) {
        User user = getAuthenticatedUser();

        if (favoriteRepository.existsByUserIdAndDirectorName(user.getId(), directorName)) {
            throw new IllegalStateException("El director ya está en favoritos");
        }

        UserFavoriteDirector favorite = UserFavoriteDirector.builder()
                .user(user)
                .directorName(directorName)
                .build();

        favoriteRepository.save(favorite);
    }

    // Quitar like
    public void removeFavoriteDirector(String directorName) {
        User user = getAuthenticatedUser();

        if (!favoriteRepository.existsByUserIdAndDirectorName(user.getId(), directorName)) {
            throw new IllegalStateException("El director no está en favoritos");
        }

        favoriteRepository.deleteByUserIdAndDirectorName(user.getId(), directorName);
    }

    // Directores favoritos del usuario autenticado
    public List<String> getFavoriteDirectors() {
        User user = getAuthenticatedUser();
        return favoriteRepository.findByUserId(user.getId())
                .stream()
                .map(UserFavoriteDirector::getDirectorName)
                .toList();
    }
}