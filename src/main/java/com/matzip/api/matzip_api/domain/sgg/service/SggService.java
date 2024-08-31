package com.matzip.api.matzip_api.domain.sgg.service;

import static com.matzip.api.matzip_api.global.error.ErrorCode.SGG_DATA_ISEMPTY;

import com.matzip.api.matzip_api.domain.sgg.dto.findall.SggDetailDto;
import com.matzip.api.matzip_api.domain.sgg.dto.findall.SggResponseDto;
import com.matzip.api.matzip_api.domain.sgg.entity.Sgg;
import com.matzip.api.matzip_api.domain.sgg.repository.SggRepository;
import com.matzip.api.matzip_api.global.exception.CustomException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SggService {

    private final SggRepository sggRepository;

    /** 시/도별 시군구 목록 조회 메서드
     *
     * @return 시/도별 시군구 이름,위도,경도 목록을 반환
     * @throws CustomException DB가 비어 있는 경우
     * */
    public List<SggResponseDto> getSggList(){
        List<String> dosiList = sggRepository.findDistinctDosi();
        if (dosiList.isEmpty()) throw new CustomException(SGG_DATA_ISEMPTY);

        return dosiList.stream()
            .map(dosi -> {
                List<Sgg> sggs = sggRepository.findByDoSi(dosi);
                List<SggDetailDto> detailDtos = sggs.stream()
                    .map(sgg -> new SggDetailDto(sgg.getSsg(),sgg.getLogt(),sgg.getLat()))
                    .collect(Collectors.toList());
                return new SggResponseDto(dosi, detailDtos);
            })
            .collect(Collectors.toList());
    }
}
