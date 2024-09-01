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
import lombok.Setter;

@Entity
@Table(name="restrt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restrt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sigunNm;
    String sigunCd;
    private String bizplcNm;
    private String licensgDe;
    private String bsnStateNm;
    private String clsbizDe;
    private String locplcAr;
    private String gradFacltDivNm;
    private Integer maleEnflpsnCnt;
    private String yy;
    private String multiUseBizestblYn;
    private String gradDivNm;
    private String totFacltScale;
    private Integer femaleEnflpsnCnt;
    private String bsnsiteCircumfrDivNm;
    private String sanittnIndutypeNm;
    private String sanittnBizcondNm;
    private Integer totEmplyCnt;
    private String refineRoadnmAddr;
    private String refineLotnoAddr;
    private String refineZipCd;
    @Column(name = "refine_wgs84_lat", precision = 8, scale = 6)
    private BigDecimal refineWgs84Lat;
    @Column(name = "refine_wgs84_logt", precision = 9, scale = 6)
    private BigDecimal refineWgs84Logt;
    private double review;
    private String restrtNm;

    @OneToMany(mappedBy = "restrt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // 평점 평균을 추가하는 편의 메서드
    public void setReview(double review) {
        this.review = review;
    }
}
