package com.matzip.api.matzip_api.domain.restrt.repository;

import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrtRepository extends JpaRepository<Restrt, Long> {
    Optional<Restrt> findByRestrtNm(String restrtNm);

    @Query("SELECT r.restrtNm FROM Restrt r")
    List<String> findAllRestrtNms();
}



