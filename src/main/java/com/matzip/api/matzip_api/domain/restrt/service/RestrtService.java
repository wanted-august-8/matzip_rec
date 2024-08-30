package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Scheduled(cron = "0 0 6 * * ?")
    // 50초마다 실행되는 테스트 코드
//    @Scheduled(fixedRate = 50000)
    public void fetchData() throws Exception {
        List<String> categories = List.of("Genrestrtjpnfood", "Genrestrtchifood", "Genrestrtfastfood");

        for (String category : categories) {
            List<RestrtDTO> restaurants = openApiService.fetchAllData(category);

            for (RestrtDTO dto : restaurants) {
                Optional<Restrt> existingRestrt = restrtRepository.findByRestrtNm(dto.getRestrtNm());
                if (existingRestrt.isPresent()) {
                    System.out.println("이미 저장된 식당입니다: " + dto.getBizplcNm());
                    continue;
                }

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
