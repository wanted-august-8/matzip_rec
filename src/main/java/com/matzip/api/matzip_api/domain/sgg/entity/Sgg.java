package com.matzip.api.matzip_api.domain.sgg.entity;

import com.matzip.api.matzip_api.domain.dosi.entity.DoSi;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dosi_id")
    private DoSi dosi;

    private String name;

    private double logt; //경도

    private double lat; //위도
}
