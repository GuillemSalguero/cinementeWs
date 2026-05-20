package com.app.auth.service;

import com.app.auth.dto.MovieDtos.*;
import com.app.auth.entity.*;
import com.app.auth.repository.*;
import com.app.auth.security.AuthenticatedUserHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final UserFavoriteMovieRepository favoriteRepo;
    private final UserWatchlistRepository     watchlistRepo;
    private final UserMovieReviewRepository   reviewRepo;
    private final UserRepository              userRepo;
    private final AuthenticatedUserHelper     authHelper;
    private final MovieRepository             movieRepo;

    public MovieService(
            UserFavoriteMovieRepository favoriteRepo,
            UserWatchlistRepository watchlistRepo,
            UserMovieReviewRepository reviewRepo,
            MovieRepository movieRepo,
            UserRepository userRepo,
            AuthenticatedUserHelper authHelper
    ) {
        this.favoriteRepo  = favoriteRepo;
        this.watchlistRepo = watchlistRepo;
        this.reviewRepo    = reviewRepo;
        this.movieRepo     = movieRepo;
        this.userRepo      = userRepo;
        this.authHelper    = authHelper;
    }

    // ═══════════════════════════════════════════════════════════
    // FAVORITOS (me gusta)
    // ═══════════════════════════════════════════════════════════

    /** Lista todos los favoritos del usuario autenticado. */
    public List<FavoriteResponse> getMyFavorites() {
        Long userId = authHelper.currentUserId();
        return favoriteRepo.findByIdUserId(userId).stream()
                .map(f -> new FavoriteResponse(
                        f.getId().getUserId(),
                        f.getId().getMovieLink(),
                        f.getCreatedAt()))
                .toList();
    }

    /** Añade una película a favoritos. Idempotente: no falla si ya existe. */
    @Transactional
    public FavoriteResponse addFavorite(String movieLink) {
        Long userId = authHelper.currentUserId();
        if (favoriteRepo.existsByIdUserIdAndIdMovieLink(userId, movieLink)) {
            // Ya existe — devolver el existente
            return favoriteRepo.findByIdUserId(userId).stream()
                    .filter(f -> f.getId().getMovieLink().equals(movieLink))
                    .findFirst()
                    .map(f -> new FavoriteResponse(userId, movieLink, f.getCreatedAt()))
                    .orElseThrow();
        }

        User user = userRepo.findById(userId).orElseThrow();
        Movie movie = movieRepo.findByRottenTomatoesLink(movieLink).orElseThrow(() -> new IllegalArgumentException("Película no encontrada en la base de datos"));
        

        UserFavoriteMovie fav = UserFavoriteMovie.builder()
                .id(new UserMovieKey(userId, movieLink))
                .user(user)
                .movie(movie)
                .build();

        UserFavoriteMovie saved = favoriteRepo.save(fav);
        return new FavoriteResponse(userId, movieLink, saved.getCreatedAt());
    }

    /** Elimina una película de favoritos. */
    @Transactional
    public void removeFavorite(String movieLink) {
        Long userId = authHelper.currentUserId();
        if (!favoriteRepo.existsByIdUserIdAndIdMovieLink(userId, movieLink)) {
            throw new IllegalArgumentException("La película no está en tus favoritos");
        }
        favoriteRepo.deleteByIdUserIdAndIdMovieLink(userId, movieLink);
    }

    // ═══════════════════════════════════════════════════════════
    // WATCHLIST
    // ═══════════════════════════════════════════════════════════

    /** Lista el watchlist del usuario autenticado. */
    public List<WatchlistResponse> getMyWatchlist() {
        Long userId = authHelper.currentUserId();
        return watchlistRepo.findByIdUserId(userId).stream()
                .map(w -> new WatchlistResponse(
                        w.getId().getUserId(),
                        w.getId().getMovieLink(),
                        w.getCreatedAt()))
                .toList();
    }

    /** Añade una película al watchlist. */
    @Transactional
    public WatchlistResponse addToWatchlist(String movieLink) {
        Long userId = authHelper.currentUserId();

        if (watchlistRepo.existsByIdUserIdAndIdMovieLink(userId, movieLink)) {
            return watchlistRepo.findByIdUserId(userId).stream()
                    .filter(w -> w.getId().getMovieLink().equals(movieLink))
                    .findFirst()
                    .map(w -> new WatchlistResponse(userId, movieLink, w.getCreatedAt()))
                    .orElseThrow();
        }

        User user = userRepo.findById(userId).orElseThrow();
        UserWatchlist entry = UserWatchlist.builder()
                .id(new UserMovieKey(userId, movieLink))
                .user(user)
                .build();

        UserWatchlist saved = watchlistRepo.save(entry);
        return new WatchlistResponse(userId, movieLink, saved.getCreatedAt());
    }

    /** Elimina una película del watchlist. */
    @Transactional
    public void removeFromWatchlist(String movieLink) {
        Long userId = authHelper.currentUserId();
        if (!watchlistRepo.existsByIdUserIdAndIdMovieLink(userId, movieLink)) {
            throw new IllegalArgumentException("La película no está en tu watchlist");
        }
        watchlistRepo.deleteByIdUserIdAndIdMovieLink(userId, movieLink);
    }

    // ═══════════════════════════════════════════════════════════
    // REVIEWS
    // ═══════════════════════════════════════════════════════════

    /** Lista todas las reviews del usuario autenticado. */
    public List<ReviewResponse> getPublicReviews(String movieLink) {
            return reviewRepo.findTop12ByIdMovieLinkOrderByCreatedAtDesc(movieLink).stream()
                    .map(r -> new ReviewResponse(
                            r.getId().getUserId(),
                            r.getId().getMovieLink(),
                            r.getRating(),
                            r.getReviewText(),
                            r.getCreatedAt()))
                    .toList();
        }

    /** Crea o actualiza la review del usuario para una película. */
    @Transactional
    public ReviewResponse upsertReview(ReviewRequest request) {
        Long userId = authHelper.currentUserId();
        UserMovieKey key = new UserMovieKey(userId, request.getMovieLink());

        UserMovieReview review = reviewRepo.findByIdUserIdAndIdMovieLink(userId, request.getMovieLink())
                .orElseGet(() -> {
                    User user = userRepo.findById(userId).orElseThrow();
                    return UserMovieReview.builder().id(key).user(user).build();
                });

        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());

        UserMovieReview saved = reviewRepo.save(review);
        return new ReviewResponse(
                userId,
                saved.getId().getMovieLink(),
                saved.getRating(),
                saved.getReviewText(),
                saved.getCreatedAt());
    }

    /** Elimina la review del usuario para una película. */
    @Transactional
    public void deleteReview(String movieLink) {
        Long userId = authHelper.currentUserId();
        if (!reviewRepo.existsByIdUserIdAndIdMovieLink(userId, movieLink)) {
            throw new IllegalArgumentException("No tienes review para esta película");
        }
        reviewRepo.deleteByIdUserIdAndIdMovieLink(userId, movieLink);
    }
}
