package com.matzip.api.matzip_api.domain.restrt.repository;

import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrtRepository extends JpaRepository<Restrt, Long> {
    Optional<Restrt> findByRestrtNm(String restrtNm);
}
