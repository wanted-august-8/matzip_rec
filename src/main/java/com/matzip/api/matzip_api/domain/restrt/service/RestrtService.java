package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestrtService {
    private final OpenApiService openApiService;
    private final RestrtRepository restrtRepository;

    @Autowired
    public RestrtService(OpenApiService openApiService, RestrtRepository restrtRepository) {
        this.openApiService = openApiService;
        this.restrtRepository = restrtRepository;
    }

    @Transactional
    public void fetchData() throws Exception {
        List<String> categories = List.of("Genrestrtjpnfood", "Genrestrtchifood", "Genrestrtfastfood");

        for (String category : categories) {
            System.out.println("Fetching data for category: " + category);
            List<RestrtDTO> restaurants = openApiService.fetchAllData(category);
            System.out.println("Number of restaurants fetched: " + restaurants.size());
            for (RestrtDTO dto : restaurants) {
                Restrt restrt = new Restrt();
                restrt.setSigunNm(dto.getSigunNm() != null ? dto.getSigunNm() : "");
                restrt.setSigunCd(dto.getSigunCd() != null ? dto.getSigunCd() : "");
                restrt.setBizplcNm(dto.getBizplcNm() != null ? dto.getBizplcNm() : "");
                restrt.setLicensgDe(dto.getLicensgDe() != null ? dto.getLicensgDe() : "");
                restrt.setBsnStateNm(dto.getBsnStateNm() != null ? dto.getBsnStateNm() : "");
                restrt.setClsbizDe(dto.getClsbizDe() != null ? dto.getClsbizDe() : "");
                restrt.setLocplcAr(dto.getLocplcAr() != null ? dto.getLocplcAr() : "");
                restrt.setGradFacltDivNm(dto.getGradFacltDivNm() != null ? dto.getGradFacltDivNm() : "");
                restrt.setMaleEnflpsnCnt(dto.getMaleEnflpsnCnt() != null ? dto.getMaleEnflpsnCnt() : -1);
                restrt.setYy(dto.getYy() != null ? dto.getYy() : "");
                restrt.setMultiUseBizestblYn(dto.getMultiUseBizestblYn() != null ? dto.getMultiUseBizestblYn() : "");
                restrt.setGradDivNm(dto.getGradDivNm() != null ? dto.getGradDivNm() : "");
                restrt.setTotFacltScale(dto.getTotFacltScale() != null ? dto.getTotFacltScale() : "");
                restrt.setFemaleEnflpsnCnt(dto.getFemaleEnflpsnCnt() != null ? dto.getFemaleEnflpsnCnt() : -1);
                restrt.setBsnsiteCircumfrDivNm(dto.getBsnsiteCircumfrDivNm() != null ? dto.getBsnsiteCircumfrDivNm() : "");
                restrt.setSanittnIndutypeNm(dto.getSanittnIndutypeNm() != null ? dto.getSanittnIndutypeNm() : "");
                restrt.setSanittnBizcondNm(dto.getSanittnBizcondNm() != null ? dto.getSanittnBizcondNm() : "");
                restrt.setTotEmplyCnt(dto.getTotEmplyCnt() != null ? dto.getTotEmplyCnt() : -1);
                restrt.setRefineRoadnmAddr(dto.getRefineRoadnmAddr() != null ? dto.getRefineRoadnmAddr() : "");
                restrt.setRefineLotnoAddr(dto.getRefineLotnoAddr() != null ? dto.getRefineLotnoAddr() : "");
                restrt.setRefineZipCd(dto.getRefineZipCd() != null ? dto.getRefineZipCd() : "");
                restrt.setRefineWgs84Lat(dto.getRefineWgs84Lat() != null ?
                    new BigDecimal(String.valueOf(dto.getRefineWgs84Lat())).setScale(6, RoundingMode.DOWN) :
                    BigDecimal.ZERO);
                restrt.setRefineWgs84Logt(dto.getRefineWgs84Logt() != null ?
                    new BigDecimal(String.valueOf(dto.getRefineWgs84Logt())).setScale(6, RoundingMode.DOWN) :
                    BigDecimal.ZERO);
                restrt.setReview(0.0);
                restrt.setRestrtNm((dto.getBizplcNm() != null ? dto.getBizplcNm() : "") + " " + (dto.getRefineRoadnmAddr() != null ? dto.getRefineRoadnmAddr() : ""));

                restrtRepository.save(restrt);
            }
        }
    }
}
