package com.matzip.api.matzip_api.domain.restrt.entity;

import com.matzip.api.matzip_api.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="restrt")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restrt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sigun_nm;
    private String sigun_cd;
    private String bizplc_nm;
    private String licensg_de;
    private String bsn_state_nm;
    private String clsbiz_de;
    private String locplc_ar;
    private String grad_faclt_div_nm;
    private Integer male_enflpsn_cnt;
    private String yy;
    private String multi_use_bizestbl_yn;
    private String grad_div_nm;
    private String tot_faclt_scale;
    private Integer female_enflpsn_cnt;
    private String bsnsite_circumfr_div_nm;
    private String sanittn_indutype_nm;
    private String sanittn_bizcond_nm;
    private Integer tot_emply_cnt;
    private String refine_roadnm_addr;
    private String refine_lotno_addr;
    private String refine_zip_cd;
    @Column(precision = 8, scale = 6)
    private BigDecimal refine_wgs84_lat;
    @Column(precision = 9, scale = 6)
    private BigDecimal refine_wgs84_logt;
    private double review;
    private String restrtNm;

    @OneToMany(mappedBy = "restrt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // 평점 평균을 추가하는 편의 메서드
    public void setReview(double review) {
        this.review = review;
    }
}
