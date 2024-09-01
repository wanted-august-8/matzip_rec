package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.dto.findlist.RestrtListByResponseDto;
import com.matzip.api.matzip_api.domain.restrt.entity.Restrt;
import com.matzip.api.matzip_api.domain.restrt.repository.RestrtRepository;
import com.matzip.api.matzip_api.domain.review.dto.ReviewResponseDto;
import com.matzip.api.matzip_api.domain.review.entity.Review;
import com.matzip.api.matzip_api.global.CommonResponse;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestrtService {

    private final RestrtRepository restrtRepository;

    public static final double EARTH_RADIUS_KM = 6371.0; //지구의 평균 반지름

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
     * 반경 내 맛집 목록 조회
     *
     * @param lat 검색할 위도
     * @param lon 검색할 경도
     * @param range 검색할 범위
     * @param sort 정렬(거리순, 평점순)
     * @return 맛집 목록
     * */
    public List<RestrtListByResponseDto> getRestrtListByLatAndLon(String lat, String lon, double range, String sort) {
        double parseDoublelat = Double.parseDouble(lat);
        double parseDoublelon = Double.parseDouble(lon);

        double[] boundingCoordinates = getBoundingCoordinates(parseDoublelat, parseDoublelon, range);
        double bounding_maxLat = boundingCoordinates[0];
        double bounding_minLat = boundingCoordinates[1];
        double bounding_maxLon = boundingCoordinates[2];
        double bounding_minLon = boundingCoordinates[3];

        List<Restrt> restrts = null;
        List<RestrtListByResponseDto> responseDtos = new ArrayList<>();

        if (sort.equals("distance")) {
            restrts = restrtRepository.findByLocation(bounding_minLat, bounding_maxLat,
                bounding_minLon, bounding_maxLon);

            if(restrts == null) return responseDtos;

            // 거리순으로 정렬
            restrts.sort(Comparator.comparingDouble(restrt ->
                calculateDistance(parseDoublelat, parseDoublelon,
                    restrt.getRefine_wgs84_lat().doubleValue(),
                    restrt.getRefine_wgs84_logt().doubleValue())
            ));

            for (Restrt restrt: restrts){
                responseDtos.add(new RestrtListByResponseDto(restrt));
            }

        }
        if (sort.equals("rating")) {
            restrts = restrtRepository.findByLocationOrderByReview(bounding_minLat, bounding_maxLat,
                bounding_minLon, bounding_maxLon);

            if(restrts == null) return responseDtos;

            for (Restrt restrt: restrts){
                responseDtos.add(new RestrtListByResponseDto(restrt));
            }
        }
        return responseDtos;
    }



    /**
     * 주어진 위도와 경도를 기준으로 주어진 반경 내의 범위 좌표를 계산
     *
     * @param lat 중심 위도
     * @param lon 중심 경도
     * @param range 반경 (킬로미터)
     * @return [최대 위도, 최소 위도, 최대 경도, 최소 경도]
     */
    public double[] getBoundingCoordinates(double lat, double lon, double range){
        double dLat = range/EARTH_RADIUS_KM;
        double dLon = range/(EARTH_RADIUS_KM * Math.cos(Math.toRadians(lat)));

        double maxLat = lat + Math.toDegrees(dLat);
        double minLat = lat - Math.toDegrees(dLat);
        double maxLon = lon + Math.toDegrees(dLon);
        double minLon = lon - Math.toDegrees(dLon);

        maxLat = roundToSixDecimalPlaces(maxLat);
        minLat = roundToSixDecimalPlaces(minLat);
        maxLon = roundToSixDecimalPlaces(maxLon);
        minLon = roundToSixDecimalPlaces(minLon);

        return new double[]{maxLat,minLat,maxLon,minLon};
    }
    /**
     * 위도,경도 소수점 6번째 자리로 반올림
     *
     * @param value 소수점 6번째 반올림 할 변수
     * @return 반올림 한 결과값
     * */
    private double roundToSixDecimalPlaces(double value) {
        return Math.round(value * 1_000_000.0) / 1_000_000.0;
    }
    /**
     * 두 지점 사이의 거리 계산(Haversine formula)
     *
     * @param lat1 중심 위도
     * @param lon1 중심 경도
     * @param lat2 반경 내 위도
     * @param lon2 반경 내 경도
     * @return 두 지점 간의 거리 (킬로미터 단위)
     * */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c; // 두 지점 간의 거리 (킬로미터 단위)
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

