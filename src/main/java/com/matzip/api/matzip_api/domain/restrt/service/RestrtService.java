package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import com.matzip.api.matzip_api.domain.review.dto.ReviewResponseDto;
import com.matzip.api.matzip_api.domain.review.entity.Review;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestrtService {

    private final RestrtRepository restrtRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY_PREFIX = "restrt:";
    private static final long CACHE_EXPIRATION = 600; // 600초 = 10분


    /**
     * 맛집의 상세 정보를 조회하는 메서드
     *
     * @param id 조회할 맛집의 ID
     * @return 맛집 상세 정보와 해당 맛집의 리뷰 리스트를 포함한 응답
     * @throws CustomException 맛집이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public CommonResponse<RestrtDetailResponseDto> getRestrtDetail(Long id) {
        String cacheKey = CACHE_KEY_PREFIX + id;

        // 캐시에서 데이터 조회
        RestrtDetailResponseDto cachedData = (RestrtDetailResponseDto) redisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            return CommonResponse.ok("맛집 상세 정보를 반환합니다. (캐시)", cachedData);
        }

        // 캐시에 데이터가 없는 경우 DB에서 조회
        // ID로 맛집 정보를 조회하고 동시에 리뷰도 함께 가져옴
        Restrt restrt = restrtRepository.findWithReviewsAndUsersById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.RESTRT_NOT_FOUND));

        // 맛집의 리뷰들을 DTO로 변환하고, 최신순으로 정렬
        List<ReviewResponseDto> reviews = restrt.getReviews().stream()
            .map(this::mapToReviewDto)
            .collect(Collectors.toList());

        // RestrtDetailResponseDto 객체를 생성
        RestrtDetailResponseDto responseDto = buildRestrtDetailResponseDto(restrt, reviews);

        // 데이터를 캐시에 저장
        if (restrt.getReviews().size() >= 1) { // 리뷰가 1개 이상일 경우에만 데이터 저장
            redisTemplate.opsForValue().set(cacheKey, responseDto, CACHE_EXPIRATION, TimeUnit.SECONDS);
        }

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
            .sigun_nm(restrt.getSigun_nm())
            .sigun_cd(restrt.getSigun_cd())
            .bizplc_nm(restrt.getBizplc_nm())
            .licensg_de(restrt.getLicensg_de())
            .bsn_state_nm(restrt.getBsn_state_nm())
            .clsbiz_de(restrt.getClsbiz_de())
            .locplc_ar(restrt.getLocplc_ar())
            .grad_faclt_div_nm(restrt.getGrad_faclt_div_nm())
            .male_enflpsn_cnt(restrt.getMale_enflpsn_cnt())
            .yy(restrt.getYy())
            .multi_use_bizestbl_yn(restrt.getMulti_use_bizestbl_yn())
            .grad_div_nm(restrt.getGrad_div_nm())
            .tot_faclt_scale(restrt.getTot_faclt_scale())
            .female_enflpsn_cnt(restrt.getFemale_enflpsn_cnt())
            .bsnsite_circumfr_div_nm(restrt.getBsnsite_circumfr_div_nm())
            .sanittn_indutype_nm(restrt.getSanittn_indutype_nm())
            .sanittn_bizcond_nm(restrt.getSanittn_bizcond_nm())
            .tot_emply_cnt(restrt.getTot_emply_cnt())
            .refine_roadnm_addr(restrt.getRefine_roadnm_addr())
            .refine_lotno_addr(restrt.getRefine_lotno_addr())
            .refine_zip_cd(restrt.getRefine_zip_cd())
            .refine_wgs84_lat(restrt.getRefine_wgs84_lat())
            .refine_wgs84_logt(restrt.getRefine_wgs84_logt())
            .review(restrt.getReview())
            .restrt_nm(restrt.getRestrt_nm())
            .reviews(reviews)
            .build();
    }
}

