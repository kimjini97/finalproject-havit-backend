package com.havit.finalbe.controller;

import com.havit.finalbe.dto.CertifyDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.CertifyService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Api(tags = {"인증샷 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/certify")
public class CertifyController {

    private final CertifyService certifyService;

    @Operation(summary = "인증 생성", description = "인증 관련 정보 기입 후 인증이 생성됩니다.")
    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseDto<CertifyDto.Response> createCertify(@ModelAttribute CertifyDto.Request certifyRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return certifyService.createCertify(certifyRequestDto, userDetails);
    }

    @Operation(summary = "인증 상세조회", description = "certifyId에 해당하는 인증을 조회합니다.")
    @GetMapping("/{certifyId}")
    public ResponseDto<CertifyDto.Response> getCertifyDetail(@PathVariable Long certifyId) {
        return certifyService.getCertifyDetail(certifyId);
    }

    @Operation(summary = "인증 수정", description = "certifyId에 해당하는 인증을 수정합니다.")
    @PatchMapping(value = "/{certifyId}", consumes = {"multipart/form-data"})
    public ResponseDto<CertifyDto.Response> updateCertify(@PathVariable Long certifyId,
                                        @ModelAttribute CertifyDto.Request certifyRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return certifyService.updateCertify(certifyId, certifyRequestDto, userDetails);
    }

    @Operation(summary = "인증 삭제", description = "certifyId에 해당하는 인증을 삭제합니다.")
    @DeleteMapping("/{certifyId}")
    public ResponseDto<String> deleteCertify(@PathVariable Long certifyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return certifyService.deleteCertify(certifyId, userDetails);
    }
}
