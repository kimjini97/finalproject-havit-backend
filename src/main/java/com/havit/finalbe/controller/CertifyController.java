package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.CertifyRequestDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.CertifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CertifyController {

    private final CertifyService certifyService;

    @PostMapping("/api/auth/certify")
    public ResponseDto<?> createCertify(@ModelAttribute CertifyRequestDto certifyRequestDto,
                                        HttpServletRequest request) throws IOException {
        return certifyService.createCertify(certifyRequestDto, request);
    }

    @GetMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> getCertifyDetail(@PathVariable Long certifyId) {
        return certifyService.getCertifyDetail(certifyId);
    }

    @PutMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> updateCertify(@PathVariable Long certifyId, @ModelAttribute CertifyRequestDto certifyRequestDto,
                                        HttpServletRequest request) throws IOException {
        return certifyService.updateCertify(certifyId, certifyRequestDto, request);
    }

    @DeleteMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> deleteCertify(@PathVariable Long certifyId, HttpServletRequest request) {
        return certifyService.deleteCertify(certifyId, request);
    }
}
