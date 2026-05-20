package com.app.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Tabla de watchlist — pendiente de crear en Supabase.
 * Ver migration SQL al final del archivo.
 */
@Entity
@Table(name = "\"UserWatchlist\"", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWatchlist {

    @EmbeddedId
    private UserMovieKey id;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}

/*
 * =============================================
 * SQL Migration — ejecutar en Supabase SQL Editor
 * =============================================
 *
 * CREATE TABLE IF NOT EXISTS public."UserWatchlist" (
 *   user_id    BIGINT NOT NULL,
 *   movie_link TEXT   NOT NULL,
 *   created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
 *   CONSTRAINT userwatchlist_pkey PRIMARY KEY (user_id, movie_link),
 *   CONSTRAINT userwatchlist_movie_fkey FOREIGN KEY (movie_link)
 *     REFERENCES "Movies" (rotten_tomatoes_link) ON DELETE CASCADE,
 *   CONSTRAINT userwatchlist_user_fkey FOREIGN KEY (user_id)
 *     REFERENCES "user" (id) ON DELETE CASCADE
 * );
 *
 * =============================================
 */
