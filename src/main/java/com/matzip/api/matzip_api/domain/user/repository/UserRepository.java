package com.matzip.api.matzip_api.domain.user.repository;

import com.matzip.api.matzip_api.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAccount(String account);

    Optional<User> findByAccount(String username);

    @Query("SELECT u FROM User u WHERE u.useLunchRecommendation = true AND u.lat IS NOT NULL AND u.logt IS NOT NULL")
    List<User> findUsersWithLunchRecommendationAndLocation();
}
