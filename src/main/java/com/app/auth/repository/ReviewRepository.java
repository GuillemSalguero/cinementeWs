package com.app.auth.repository;

import com.app.auth.entity.Review;
import com.app.auth.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

    List<Review> findTop12ByMovieLinkOrderByCreatedAtDesc(String movieLink);
}