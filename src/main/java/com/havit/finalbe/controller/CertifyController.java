package com.havit.finalbe.controller;

import com.havit.finalbe.dto.CertifyDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.CertifyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/certify")
public class CertifyController {

    private final CertifyService certifyService;

    @Operation(summary = "인증 생성", description = "인증 관련 정보 기입후 인증이 생성됩니다.")
    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseDto<?> createCertify(@ModelAttribute CertifyDto.Request certifyRequestDto, HttpServletRequest request) throws IOException {
        return certifyService.createCertify(certifyRequestDto, request);
    }

    @Operation(summary = "인증 상세조회", description = "certifyId에 해당하는 인증을 조회합니다.")
    @GetMapping("/{certifyId}")
    public ResponseDto<?> getCertifyDetail(@PathVariable Long certifyId) {
        return certifyService.getCertifyDetail(certifyId);
    }

    @Operation(summary = "인증 수정", description = "certifyId에 해당하는 인증을 수정합니다.")
    @PutMapping(value = "/{certifyId}", consumes = {"multipart/form-data"})
    public ResponseDto<?> updateCertify(@PathVariable Long certifyId,
                                        @ModelAttribute CertifyDto.Request certifyRequestDto, HttpServletRequest request) throws IOException {
        return certifyService.updateCertify(certifyId, certifyRequestDto, request);
    }

    @Operation(summary = "인증 삭제", description = "certifyId에 해당하는 인증을 삭제합니다.")
    @DeleteMapping("/{certifyId}")
    public ResponseDto<?> deleteCertify(@PathVariable Long certifyId, HttpServletRequest request) {
        return certifyService.deleteCertify(certifyId, request);
    }
}
