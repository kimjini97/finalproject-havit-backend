package com.havit.finalbe.service;

import com.havit.finalbe.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class ServiceUtil {

    // 토큰 선언 ex.TokenProvider

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.tokenValidation(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
