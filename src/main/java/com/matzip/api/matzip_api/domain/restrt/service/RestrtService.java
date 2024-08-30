package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
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
        List<String> categories = List.of("Genrestrtjpnfood", "GenrestrtChifood", "GenrestrtFastfood");

        for (String category : categories) {
            List<RestrtDTO> restaurants = openApiService.fetchAllData(category);
            for (RestrtDTO dto : restaurants) {
                Restrt restrt = new Restrt();
                restrt.setSigunNm(dto.getSigunNm() != null ? dto.getSigunNm() : "");
                restrt.setSigunCd(dto.getSigunCd() != null ? dto.getSigunCd() : "");
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
                restrt.setRefineLotnoAddr(dto.getRefineLotnoAddr() != null ? dto.getRefineLotnoAddr() : "");
                restrt.setRefineZipCd(dto.getRefineZipCd() != null ? dto.getRefineZipCd() : "");
                restrt.setReview(0.0);
                restrt.setRestrtNm((dto.getBizplcNm() != null ? dto.getBizplcNm() : "") + " " + (dto.getRefineRoadnmAddr() != null ? dto.getRefineRoadnmAddr() : ""));

                restrtRepository.save(restrt);
            }
        }
    }
}
