package com.havit.finalbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CertifyRequestDto {
    private Long groupId;
    private String title;
    private MultipartFile imgFile;
    private double longitude;
    private double latitude;

}
