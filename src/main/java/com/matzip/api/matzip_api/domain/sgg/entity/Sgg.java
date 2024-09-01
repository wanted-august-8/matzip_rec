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
import lombok.Setter;

@Entity
@Table(name="sgg")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sgg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doSi; //시/도

    private String ssg; //시군구

    private double logt; //경도 lon

    private double lat; //위도

    public Sgg(String doSi,String ssg, double logt,double lat){
        this.doSi = doSi;
        this.ssg = ssg;
        this.logt = logt;
        this.lat = lat;
    }
}
