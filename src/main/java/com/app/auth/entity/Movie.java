package com.app.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Movies\"", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @Column(name = "rotten_tomatoes_link")
    private String rottenTomatoesLink;

    @Column(name = "movie_title")
    private String movieTitle;

    @Column(name = "movie_info", columnDefinition = "TEXT")
    private String movieInfo;

    @Column(name = "critics_consensus", columnDefinition = "TEXT")
    private String criticsConsensus;

    @Column(name = "content_rating")
    private String contentRating;

    @Column(name = "genres")
    private String genres;

    @Column(name = "directors")
    private String directors;

    @Column(name = "authors")
    private String authors;

    @Column(name = "actors")
    private String actors;

    @Column(name = "original_release_date")
    private String originalReleaseDate;

    @Column(name = "streaming_release_date")
    private String streamingReleaseDate;

    @Column(name = "runtime")
    private Long runtime;

    @Column(name = "production_company")
    private String productionCompany;

    @Column(name = "tomatometer_status")
    private String tomatometerStatus;

    @Column(name = "tomatometer_rating")
    private Long tomatometerRating;

    @Column(name = "tomatometer_count")
    private Long tomatometerCount;

    @Column(name = "audience_status")
    private String audienceStatus;

    @Column(name = "audience_rating")
    private Long audienceRating;

    @Column(name = "audience_count")
    private Long audienceCount;

    @Column(name = "tomatometer_top_critics_count")
    private String tomatometerTopCriticsCount;

    @Column(name = "tomatometer_fresh_critics_count")
    private Long tomatometerFreshCriticsCount;

    @Column(name = "tomatometer_rotten_critics_count")
    private String tomatometerRottenCriticsCount;

    @Column(name = "tmdb_id")
    private String tmdbId;
    
    @Column(name = "poster_url")
    private String poster_url;
}