package com.matzip.api.matzip_api.domain.sgg.service;

import static com.matzip.api.matzip_api.global.error.ErrorCode.FILE_READ_ERROR;
import static com.matzip.api.matzip_api.global.error.ErrorCode.SGG_DATA_ISEMPTY;

import com.matzip.api.matzip_api.domain.sgg.dto.findall.SggDetailDto;
import com.matzip.api.matzip_api.domain.sgg.dto.findall.SggResponseDto;
import com.matzip.api.matzip_api.domain.sgg.entity.Sgg;
import com.matzip.api.matzip_api.domain.sgg.repository.SggRepository;
import com.matzip.api.matzip_api.global.exception.CustomException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class SggService {

    private final SggRepository sggRepository;

    private final JdbcTemplate jdbcTemplate;

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

    /** csv파일 업로드 (bulk insert로 시간 단축)
     * 약 3s 소요
     * @param file 업로드할 csv 파일
     * @throws CustomException file 읽는 도중 오류 발생 시
     * */
    public void uploadFile(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            //기존 데이터 삭제
            jdbcTemplate.update("TRUNCATE TABLE sgg");

            // BOM제거
            reader.mark(1);
            if (reader.read() != 0xFEFF) {
                reader.reset();
            }

            String sql = "insert into sgg(do_si,ssg,logt,lat) values(?,?,?,?)";
            List<Object[]> batchArgs = new ArrayList<>();

            try (CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord csvRecord : csvParser) {
                    String doSi = csvRecord.get("do-si"); // 헤더 이름 그대로 사용
                    String csvSgg = csvRecord.get("sgg");
                    double lon = Double.parseDouble(csvRecord.get("lon"));
                    double lat = Double.parseDouble(csvRecord.get("lat"));

                    batchArgs.add(new Object[]{doSi, csvSgg, lon, lat});
                    if (batchArgs.size()%10000 == 0){
                        jdbcTemplate.batchUpdate(sql,batchArgs);
                        batchArgs.clear();
                    }
                }
                if (!batchArgs.isEmpty()) jdbcTemplate.batchUpdate(sql,batchArgs);
            }

        } catch (IOException e) {
            throw new CustomException(FILE_READ_ERROR);
        }
    }

    /** csv파일 업로드 (insert문 파일 row 개수만큼 시행) - 삭제 예정
     * 약 15s 소요
     * @param file 업로드할 csv 파일
     * */
    public void uploadFile2(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            //기존 데이터 삭제
            sggRepository.deleteAll();

            // BOM제거
            reader.mark(1);
            if (reader.read() != 0xFEFF) {
                reader.reset();
            }

            try (CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord csvRecord : csvParser) {
                    String doSi = csvRecord.get("do-si"); // 헤더 이름 그대로 사용
                    String csvSgg = csvRecord.get("sgg");
                    double lon = Double.parseDouble(csvRecord.get("lon"));
                    double lat = Double.parseDouble(csvRecord.get("lat"));

                    Sgg sgg = new Sgg(doSi, csvSgg, lon, lat);
                    sggRepository.save(sgg);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
