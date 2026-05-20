package com.app.auth.repository;

import com.app.auth.entity.UserFavoriteDirector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteDirectorRepository extends JpaRepository<UserFavoriteDirector, Long> {

    List<UserFavoriteDirector> findByUserId(Long userId);

    boolean existsByUserIdAndDirectorName(Long userId, String directorName);

    void deleteByUserIdAndDirectorName(Long userId, String directorName);
}