package com.matzip.api.matzip_api.domain.restrt.repository;

import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestrtRepository extends JpaRepository<Restrt, Long> {
    Optional<Restrt> findByRestrtNm(String restrtNm);

    @Query("SELECT r.restrtNm FROM Restrt r")
    List<String> findAllRestrtNms();

    // Restrt와 연관된 reviews 및 reviews의 User를 함께 가져오는 메서드
    @Query("SELECT r FROM Restrt r LEFT JOIN FETCH r.reviews rev LEFT JOIN FETCH rev.user WHERE r.id = :id ORDER BY rev.createdAt DESC")
    Optional<Restrt> findWithReviewsAndUsersById(Long id);
}
