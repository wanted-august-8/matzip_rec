package com.matzip.api.matzip_api.domain.restrt.dto.findlist;

import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestrtListByResponseDto {

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
    private String restrt_nm;
    private double review;

    public RestrtListByResponseDto (Restrt restrt) {
        this.id = restrt.getId();
        this.sigun_nm = restrt.getSigunNm();
        this.sigun_cd = restrt.getSigunCd();
        this.bizplc_nm = restrt.getBizplcNm();
        this.licensg_de = restrt.getLicensgDe();
        this.bsn_state_nm = restrt.getBsnStateNm();
//        this.clsbiz_de = restrt.getClsbiz_de();
//        this.locplc_ar = restrt.getLocplc_ar();
//        this.grad_faclt_div_nm = restrt.getGrad_faclt_div_nm();
//        this.male_enflpsn_cnt = restrt.getMale_enflpsn_cnt();
//        this.yy = restrt.getYy();
//        this.multi_use_bizestbl_yn = restrt.getMulti_use_bizestbl_yn();
//        this.grad_div_nm = restrt.getGrad_div_nm();
//        this.tot_faclt_scale = restrt.getTot_faclt_scale();
//        this.female_enflpsn_cnt = restrt.getFemale_enflpsn_cnt();
        this.bsnsite_circumfr_div_nm= restrt.getBsnsiteCircumfrDivNm();
        this.sanittn_indutype_nm= restrt.getSanittnIndutypeNm();
        this.sanittn_bizcond_nm = restrt.getSanittnBizcondNm();
        this.tot_emply_cnt = restrt.getTotEmplyCnt();
        this.refine_roadnm_addr = restrt.getRefineRoadnmAddr();
        this.refine_lotno_addr = restrt.getRefineLotnoAddr();
        this.refine_zip_cd = restrt.getRefineZipCd();
        this.refine_wgs84_lat = restrt.getRefineWgs84Lat();
        this.refine_wgs84_logt = restrt.getRefineWgs84Logt();
        this.restrt_nm = restrt.getRestrtNm();
        this.review = restrt.getReview();
    }
}
