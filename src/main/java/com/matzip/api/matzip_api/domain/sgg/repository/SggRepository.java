package com.matzip.api.matzip_api.domain.sgg.repository;

import com.matzip.api.matzip_api.domain.sgg.entity.Sgg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SggRepository extends JpaRepository<Sgg, Long> {
    List<Sgg> findByDoSi(String dosi);
    @Query("select distinct s.doSi from Sgg s")
    List<String> findDistinctDosi();
}
