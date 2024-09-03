package com.matzip.api.matzip_api.domain.restrt.service;

import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDTO;
import com.matzip.api.matzip_api.domain.restrt.dto.RestrtDetailResponseDto;
import com.matzip.api.matzip_api.domain.restrt.dto.findlist.RestrtListByResponseDto;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.Set;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestrtService {
    private final OpenApiService openApiService;
    private final RestrtRepository restrtRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY_PREFIX = "restrt:";
    private static final long CACHE_EXPIRATION = 600; // 600초 = 10분

    @PersistenceContext
    private EntityManager entityManager;

    public static final double EARTH_RADIUS_KM = 6371.0; //지구의 평균 반지름
  
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
                    restrt.getRefineWgs84Lat().doubleValue(),
                    restrt.getRefineWgs84Logt().doubleValue())
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
     * 알림용으로 평점순 상위 5개의 맛집 목록 조회
     *
     * @param lat 위도
     * @param lon 경도
     * @param range 범위(km)
     * @return 평점순 상위 5개의 맛집 목록
     */
    public List<RestrtListByResponseDto> getTop5RestaurantsByRating(BigDecimal lat, BigDecimal lon, double range) {
        double parseDoublelat = lat.doubleValue();
        double parseDoublelon = lon.doubleValue();

        double[] boundingCoordinates = getBoundingCoordinates(parseDoublelat, parseDoublelon, range);
        double bounding_maxLat = boundingCoordinates[0];
        double bounding_minLat = boundingCoordinates[1];
        double bounding_maxLon = boundingCoordinates[2];
        double bounding_minLon = boundingCoordinates[3];

        Pageable pageable = PageRequest.of(0, 5); // 상위 5개만 가져오기
        List<RestrtListByResponseDto> responseDtos = new ArrayList<>();

        // 평점순 상위 5개의 맛집을 가져오는 쿼리 실행
        Page<Restrt> top5Restaurants = restrtRepository.findByLocationOrderByReview(bounding_minLat, bounding_maxLat, bounding_minLon, bounding_maxLon, pageable);

        if (top5Restaurants.hasContent()) {
            for (Restrt restrt : top5Restaurants.getContent()) {
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

