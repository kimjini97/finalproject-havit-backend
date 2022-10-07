package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.CertifyRequestDto;
import com.havit.finalbe.dto.response.CertifyResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.CertifyService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(tags = {"인증샷 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/certify")
public class CertifyController {

    private final CertifyService certifyService;

    @Operation(summary = "인증 생성", description = "인증 관련 정보 기입 후 인증이 생성됩니다.")
    @PostMapping(value = "/")
    public CertifyResponseDto createCertify(@RequestBody @Valid CertifyRequestDto certifyRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return certifyService.createCertify(certifyRequestDto, userDetails);
    }

    @Operation(summary = "인증 상세조회", description = "certifyId에 해당하는 인증을 조회합니다.")
    @GetMapping("/{certifyId}")
    public CertifyResponseDto getCertifyDetail(@PathVariable Long certifyId) {
        return certifyService.getCertifyDetail(certifyId);
    }

    @Operation(summary = "인증 수정", description = "certifyId에 해당하는 인증을 수정합니다.")
    @PatchMapping(value = "/{certifyId}")
    public CertifyResponseDto updateCertify(@PathVariable Long certifyId,
                                        @RequestBody CertifyRequestDto certifyRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return certifyService.updateCertify(certifyId, certifyRequestDto, userDetails);
    }

    @Operation(summary = "인증 삭제", description = "certifyId에 해당하는 인증을 삭제합니다.")
    @DeleteMapping("/{certifyId}")
    public String deleteCertify(@PathVariable Long certifyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return certifyService.deleteCertify(certifyId, userDetails);
    }
}
