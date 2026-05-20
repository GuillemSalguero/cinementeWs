package com.app.auth.repository;

import com.app.auth.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, JpaSpecificationExecutor<Movie>, java.io.Serializable {
    
    Optional<Movie> findByRottenTomatoesLink(String name);

    // Búsqueda simple por título (para autocompletado, etc.)
    Page<Movie> findByMovieTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.directors) LIKE LOWER(CONCAT('%', :director, '%'))")
    List<Movie> findByDirector(@Param("director") String director);
}