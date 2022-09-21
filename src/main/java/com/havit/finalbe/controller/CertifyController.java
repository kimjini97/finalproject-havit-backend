package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CertifyController {

    @PostMapping("/api/auth/certify")
    public ResponseDto<?> createCertify() {
        return null;
    }

    @GetMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> getCertifyDetail(@PathVariable Long certifyId) {
        return null;
    }

    @PutMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> updateCertify(@PathVariable Long certifyId) {
        return null;
    }

    @DeleteMapping("/api/auth/certify/{certifyId}")
    public ResponseDto<?> deleteCertify(@PathVariable Long certifyId) {
        return null;
    }
}
