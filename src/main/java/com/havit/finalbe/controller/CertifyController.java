package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[인증 API]")
@RestController
@RequiredArgsConstructor
public class CertifyController {

    @Operation(summary = "인증 생성", description = "인증 관련 정보 기입후 인증이 생성됩니다.")
    @PostMapping("/api/auth/certify")
    public ResponseDto<?> createCertify() {
        return null;
    }

    @Operation(summary = "인증 상세조회", description = "certifyId에 해당하는 인증을 조회합니다.")
    @GetMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> getCertifyDetail(@PathVariable Long certifyId) {
        return null;
    }

    @Operation(summary = "인증 수정", description = "certifyId에 해당하는 인증을 수정합니다.")
    @PutMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> updateCertify(@PathVariable Long certifyId) {
        return null;
    }

    @Operation(summary = "인증 삭제", description = "certifyId에 해당하는 인증을 삭제합니다.")
    @DeleteMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> deleteCertify(@PathVariable Long certifyId) {
        return null;
    }
}
