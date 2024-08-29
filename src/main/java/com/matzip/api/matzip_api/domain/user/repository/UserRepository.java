package com.matzip.api.matzip_api.domain.user.repository;

import com.matzip.api.matzip_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
