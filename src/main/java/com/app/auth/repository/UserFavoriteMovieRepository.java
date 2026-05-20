package com.app.auth.repository;

import com.app.auth.entity.UserFavoriteMovie;
import com.app.auth.entity.UserMovieKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteMovieRepository extends JpaRepository<UserFavoriteMovie, UserMovieKey> {

    List<UserFavoriteMovie> findByIdUserId(Long userId);

    boolean existsByIdUserIdAndIdMovieLink(Long userId, String movieLink);

    void deleteByIdUserIdAndIdMovieLink(Long userId, String movieLink);
    
    @Query("""
        SELECT ufm FROM UserFavoriteMovie ufm
        JOIN FETCH ufm.movie
        ORDER BY ufm.createdAt DESC
        LIMIT 12
    """)
    List<UserFavoriteMovie> findTop12ByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("""
        SELECT ufm FROM UserFavoriteMovie ufm
        JOIN FETCH ufm.movie
        ORDER BY ufm.createdAt DESC
        LIMIT 12
    """)
    List<UserFavoriteMovie> findTop12FromFriendsNoRepeat(@Param("userId") Long userId);
}
