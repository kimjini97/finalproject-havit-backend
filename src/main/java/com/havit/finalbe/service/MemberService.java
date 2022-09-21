package com.havit.finalbe.service;

import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.jwt.util.JwtUtil;
import com.havit.finalbe.repository.MemberRepository;
import com.havit.finalbe.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto<?> sigunup() {
        return null;
    }
}
