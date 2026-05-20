package com.app.auth.repository;

import com.app.auth.entity.UserMovieKey;
import com.app.auth.entity.UserWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWatchlistRepository extends JpaRepository<UserWatchlist, UserMovieKey> {

    List<UserWatchlist> findByIdUserId(Long userId);

    boolean existsByIdUserIdAndIdMovieLink(Long userId, String movieLink);

    void deleteByIdUserIdAndIdMovieLink(Long userId, String movieLink);

}
