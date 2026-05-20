package com.app.auth.repository;

import com.app.auth.entity.UserMovieKey;
import com.app.auth.entity.UserMovieReview;
import com.app.auth.entity.UserFavoriteMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMovieReviewRepository extends JpaRepository<UserMovieReview, UserMovieKey> {

    List<UserMovieReview> findByIdUserId(Long userId);

    Optional<UserMovieReview> findByIdUserIdAndIdMovieLink(Long userId, String movieLink);

    boolean existsByIdUserIdAndIdMovieLink(Long userId, String movieLink);

    void deleteByIdUserIdAndIdMovieLink(Long userId, String movieLink);
    
    List<UserMovieReview> findTop12ByIdMovieLinkOrderByCreatedAtDesc(String movieLink);

    @Query("""
        SELECT ufm FROM UserFavoriteMovie ufm
        JOIN FETCH ufm.movie
        ORDER BY ufm.createdAt DESC
        LIMIT 12
    """)
    List<UserFavoriteMovie> findTop12ByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    // Últimas 12 pelis favoritas de los amigos, sin repetir
    @Query("""
        SELECT ufm FROM UserFavoriteMovie ufm
        JOIN FETCH ufm.movie
        ORDER BY ufm.createdAt DESC
        LIMIT 12
    """)
    List<UserFavoriteMovie> findTop12FromFriendsNoRepeat(@Param("userId") Long userId);
}