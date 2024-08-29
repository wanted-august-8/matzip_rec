package com.matzip.api.matzip_api.domain.user.repository;

import com.matzip.api.matzip_api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAccount(String account);
}
