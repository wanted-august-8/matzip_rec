package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.review.dto.ReviewResponseDto;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.review.entity.Review;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestrtService {
    private final OpenApiService openApiService;
    private final RestrtRepository restrtRepository;

    @Autowired
    public RestrtService(OpenApiService openApiService, RestrtRepository restrtRepository) {
        this.openApiService = openApiService;
        this.restrtRepository = restrtRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
//    매일 오전 6시 실행 운영 코드
    @Scheduled(cron = "0 0 6 * * ?")
//     50초마다 실행되는 테스트 코드
//    @Scheduled(fixedRate = 50000)
    public void fetchData() throws Exception {
        List<String> categories = List.of("Genrestrtjpnfood", "Genrestrtchifood", "Genrestrtfastfood");

        for (String category : categories) {
            List<RestrtDTO> restaurants = openApiService.fetchAllData(category);

            Set<String> existingRestrtNms = new HashSet<>(restrtRepository.findAllRestrtNms());

            List<Restrt> newRestaurants = restaurants.stream()
                .map(this::convertToEntity)
                .filter(restrt -> !existingRestrtNms.contains(restrt.getRestrtNm()))
                .collect(Collectors.toList());

            System.out.println("Existing restaurant names count: " + existingRestrtNms.size());
            System.out.println("New restaurants to save: " + newRestaurants.size());

            saveAllRestaurants(newRestaurants);
        }
    }

    private Restrt convertToEntity(RestrtDTO dto) {
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
        restrt.setRestrtNm(generateRestrtNm(dto));
        return restrt;
    }

    private String generateRestrtNm(RestrtDTO dto) {
        return (dto.getBizplcNm() != null ? dto.getBizplcNm() : "") + " " +
            (dto.getRefineRoadnmAddr() != null ? dto.getRefineRoadnmAddr() : "");
    }

    @Transactional
    public void saveAllRestaurants(List<Restrt> restaurants) {
        int batchSize = 50;
        for (int i = 0; i < restaurants.size(); i++) {
            entityManager.persist(restaurants.get(i));
            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
