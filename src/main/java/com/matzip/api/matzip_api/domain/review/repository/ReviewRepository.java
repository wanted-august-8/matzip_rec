package com.matzip.api.matzip_api.domain.review.repository;

import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestrt(Restrt restrt);
}
