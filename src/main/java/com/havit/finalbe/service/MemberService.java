package com.havit.finalbe.service;

import com.havit.finalbe.dto.MemberDto;
import com.havit.finalbe.dto.response.MessageResponseDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.RefreshToken;
import com.havit.finalbe.exception.*;
import com.havit.finalbe.jwt.util.JwtUtil;
import com.havit.finalbe.jwt.util.TokenProperties;
import com.havit.finalbe.repository.MemberRepository;
import com.havit.finalbe.repository.RefreshTokenRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final ServiceUtil serviceUtil;

    @Transactional
    public ResponseDto<?> sigunup(MemberDto.Signup signupRequestDto) {
        String username = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();

        if (!emailStrCheck(username)) {throw new InvalidUsernameException(ErrorMsg.INVALID_EMAIL);}
        if (!emailDuplicateCheck(username)) {throw new DuplicateUsernameException(ErrorMsg.DUPLICATE_EMAIL);}
        if (!passwordStrCheck(password)) {throw new InvalidPasswordException(ErrorMsg.INVALID_PASSWORD);}
        else {

            Member member = Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .build();
            memberRepository.save(member);

            MemberDto.Response signupInfo = MemberDto.Response.builder()
                    .memberId(member.getMemberId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .createdAt(member.getCreatedAt())
                    .build();

            return ResponseDto.success(signupInfo);
        }


    }

    @Transactional
    public ResponseDto<?> login(MemberDto.Login loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getEmail();
        Member member = isPresentMemberByUsername(username);

        if(member == null){ throw new MemberNotFoundException(ErrorMsg.MEMBER_NOT_FOUND);}

        if(!member.validatePassword(passwordEncoder,loginRequestDto.getPassword())){
            throw new MemberNotFoundException(ErrorMsg.MEMBER_NOT_FOUND);
        }

        // 토큰 발급
        String accessToken = jwtUtil.createToken(username,TokenProperties.AUTH_HEADER);
        String refreshToken = jwtUtil.createToken(username, TokenProperties.REFRESH_HEADER);

        RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);

        // 로그인 경력 있는 user (DB에 Refresh Token 있음) -> 새로 로그인 했으면 새로 발급받는 토큰으로 변경
        // 로그인 처음 하는 user (DB에 Refresh Token 없음) -> 발급받은 Refresh 토큰 저장
        if(refreshTokenFromDB == null){
            RefreshToken saveRefreshToken = RefreshToken.builder()
                    .member(member)
                    .tokenValue(refreshToken)
                    .build();
            refreshTokenRepository.save(saveRefreshToken);
        }else{
            refreshTokenFromDB.updateValue(refreshToken);
        }

        // 헤더에 응답으로 보내줌
        TokenToHeaders(response, accessToken, refreshToken);

        MemberDto.Response memberResponseDto = MemberDto.Response.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
        return ResponseDto.success(memberResponseDto);
    }

    @Transactional
    public ResponseDto<?> logout(HttpServletRequest request, UserDetailsImpl userDetails){

        Member member = userDetails.getMember();

        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);

        if(refreshHeader == null) {
            throw new NeedRefreshTokenException(ErrorMsg.NEED_REFRESH_TOKEN);}

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)) {
            throw new InvalidRefreshTokenException(ErrorMsg.INVALID_TOKEN);
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE,"");

        // 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenValidate) {
            case TokenProperties.VALID:
            case TokenProperties.EXPIRED:
                RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) {
                    refreshTokenRepository.delete(refreshTokenFromDB);
                    MessageResponseDto messageResponseDto = MessageResponseDto.builder()
                            .message("로그아웃 되었습니다.")
                            .build();
                    return ResponseDto.success(messageResponseDto);
                } else {
                    throw new InvalidRefreshTokenException(ErrorMsg.INVALID_TOKEN);
                }
            default:
                throw new InvalidRefreshTokenException(ErrorMsg.INVALID_TOKEN);
        }
    }

    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);

        if(refreshHeader == null){
            throw new NeedRefreshTokenException(ErrorMsg.NEED_REFRESH_TOKEN);
        }

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)){
            throw new InvalidRefreshTokenException(ErrorMsg.INVALID_TOKEN);
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE, "");

        // Refresh 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenValidate) {
            case TokenProperties.EXPIRED:
                throw new ExpiredRefreshTokenException(ErrorMsg.EXPIRED_REFRESH_TOKEN);
            case TokenProperties.VALID:
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                Member member = isPresentMemberByUsername(username);

                if (member == null) {
                    throw new MemberNotFoundException(ErrorMsg.MEMBER_NOT_FOUND);
                } else {
                    RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                    if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) { // new access token 발급
                        String newAccessToken = jwtUtil.createToken(member.getUsername(), TokenProperties.AUTH_HEADER);
                        response.addHeader(TokenProperties.AUTH_HEADER, TokenProperties.TOKEN_TYPE + newAccessToken);
                        return ResponseDto.success("토큰이 재발급 되었습니다.");
                    } else {
                        throw new RefreshTokenNotMatched(ErrorMsg.REFRESH_TOKEN_NOT_MATCHED);
                    }
                }
            default:
                throw new InvalidRefreshTokenException(ErrorMsg.INVALID_TOKEN);
        }
    }

    private boolean emailDuplicateCheck(String email){
        Object member = isPresentMemberByUsername(email);
        return member == null;
    }

    // 회원가입, 로그인 조건 검증

    private boolean emailStrCheck (String email){
//        return Pattern.matches("^[a-zA-Z0-9]{1,64}+@[a-zA-Z0-9]{1,100}+$", email);
        return Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){1,64}@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", email);
    }

    private boolean passwordStrCheck (String password){
        return Pattern.matches("^(?=.*\\d)[a-z\\d!@#$%^&*]{8,}$", password);
    }

    @Transactional(readOnly = true)
    public Member isPresentMemberByUsername(String username) {
        Optional<Object> optionalMember = memberRepository.findByUsername(username);
        return (Member) optionalMember.orElse(null);
    }

    private void TokenToHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(TokenProperties.AUTH_HEADER, accessToken);
        response.addHeader(TokenProperties.REFRESH_HEADER, refreshToken);
    }
}
