package com.matzip.api.matzip_api.domain.sgg.dto.findall;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SggDetailDto {

    private String ssg; //시군구

    private double logt; //경도

    private double lat; //위도
}
