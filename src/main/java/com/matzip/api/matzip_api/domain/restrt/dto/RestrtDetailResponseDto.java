package com.matzip.api.matzip_api.domain.restrt.dto;

import com.matzip.api.matzip_api.domain.review.dto.ReviewResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrtDetailResponseDto {
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
    private BigDecimal refine_wgs84_lat;
    private BigDecimal refine_wgs84_logt;
    private double review;
    private String restrt_nm;

    private List<ReviewResponseDto> reviews;
}
