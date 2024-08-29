package com.matzip.api.matzip_api.domain.sgg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="sgg")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sgg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doSi; //시/도

    private String ssg; //시군구

    private double logt; //경도

    private double lat; //위도
}
