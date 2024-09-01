package com.matzip.api.matzip_api.domain.sgg.dto.findall;

import com.matzip.api.matzip_api.domain.sgg.dto.findall.SggDetailDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SggResponseDto {
    private String doSi; //시/도

    private List<SggDetailDto> ssg;
}
