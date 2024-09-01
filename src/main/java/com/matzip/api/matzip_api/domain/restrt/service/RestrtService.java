package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import com.matzip.api.matzip_api.domain.review.dto.ReviewResponseDto;
import com.matzip.api.matzip_api.domain.review.entity.Review;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestrtService {
    private final OpenApiService openApiService;
    private final RestrtRepository restrtRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
//    매일 오전 6시 실행 운영 코드
    @Scheduled(cron = "0 0 6 * * ?")
//    50초마다 실행되는 테스트 코드
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

    /**
     * 맛집의 상세 정보를 조회하는 메서드
     *
     * @param id 조회할 맛집의 ID
     * @return 맛집 상세 정보와 해당 맛집의 리뷰 리스트를 포함한 응답
     * @throws CustomException 맛집이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public CommonResponse<RestrtDetailResponseDto> getRestrtDetail(Long id) {
        // ID로 맛집 정보를 조회하고 동시에 리뷰도 함께 가져옴
        Restrt restrt = restrtRepository.findWithReviewsAndUsersById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.RESTRT_NOT_FOUND));

        // 맛집의 리뷰들을 DTO로 변환하고, 최신순으로 정렬
        List<ReviewResponseDto> reviews = restrt.getReviews().stream()
            .map(this::mapToReviewDto)
            .collect(Collectors.toList());

        // RestrtDetailResponseDto 객체를 생성
        RestrtDetailResponseDto responseDto = buildRestrtDetailResponseDto(restrt, reviews);

        return CommonResponse.ok("맛집 상세 정보를 반환합니다.", responseDto);
    }

    /**
     * Review 엔티티를 ReviewResponseDto로 변환하는 메서드
     *
     * @param review 변환할 Review 엔티티
     * @return 변환된 ReviewResponseDto
     */
    private ReviewResponseDto mapToReviewDto(Review review) {
        return ReviewResponseDto.builder()
            .id(review.getId())
            .score(review.getScore())
            .content(review.getContent())
            .account(review.getUser().getAccount())
            .createdAt(review.getCreatedAt())
            .build();
    }

    /**
     * Restrt 엔티티와 리뷰 리스트를 사용하여 RestrtDetailResponseDto를 생성하는 메서드
     *
     * @param restrt 조회된 Restrt 엔티티
     * @param reviews 변환된 리뷰 DTO 리스트
     * @return 생성된 RestrtDetailResponseDto
     */
    private RestrtDetailResponseDto buildRestrtDetailResponseDto(Restrt restrt, List<ReviewResponseDto> reviews) {
        return RestrtDetailResponseDto.builder()
            .id(restrt.getId())
            .sigun_nm(restrt.getSigunNm())
            .sigun_cd(restrt.getSigunCd())
            .bizplc_nm(restrt.getBizplcNm())
            .licensg_de(restrt.getLicensgDe())
            .bsn_state_nm(restrt.getBsnStateNm())
            .clsbiz_de(restrt.getClsbizDe())
            .locplc_ar(restrt.getLocplcAr())
            .grad_faclt_div_nm(restrt.getGradFacltDivNm())
            .male_enflpsn_cnt(restrt.getMaleEnflpsnCnt())
            .yy(restrt.getYy())
            .multi_use_bizestbl_yn(restrt.getMultiUseBizestblYn())
            .grad_div_nm(restrt.getGradDivNm())
            .tot_faclt_scale(restrt.getTotFacltScale())
            .female_enflpsn_cnt(restrt.getFemaleEnflpsnCnt())
            .bsnsite_circumfr_div_nm(restrt.getBsnsiteCircumfrDivNm())
            .sanittn_indutype_nm(restrt.getSanittnIndutypeNm())
            .sanittn_bizcond_nm(restrt.getSanittnBizcondNm())
            .tot_emply_cnt(restrt.getTotEmplyCnt())
            .refine_roadnm_addr(restrt.getRefineRoadnmAddr())
            .refine_lotno_addr(restrt.getRefineLotnoAddr())
            .refine_zip_cd(restrt.getRefineZipCd())
            .refine_wgs84_lat(restrt.getRefineWgs84Lat())
            .refine_wgs84_logt(restrt.getRefineWgs84Logt())
            .review(restrt.getReview())
            .restrt_nm(restrt.getRestrtNm())
            .reviews(reviews)
            .build();
    }
}

