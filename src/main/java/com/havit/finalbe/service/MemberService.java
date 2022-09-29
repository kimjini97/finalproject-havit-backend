package com.havit.finalbe.service;

import com.havit.finalbe.dto.MemberDto;
import com.havit.finalbe.dto.response.MessageResponseDto;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.RefreshToken;
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
    public MemberDto.Response signup(MemberDto.Signup signupRequestDto) {
        String username = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();

        if (!emailStrCheck(username)) {throw new IllegalArgumentException("필드는 100자 이하여야 하며, @ 기호 전까지 64자 이하여야 합니다.");}
        if (!emailDuplicateCheck(username)) {throw new IllegalArgumentException("중복된 이메일 주소가 있습니다.");}
        if (!passwordStrCheck(password)) {throw new IllegalArgumentException("비밀번호 최소 8자 이상 , 소문자 , 숫자 (0-9) 또는 특수문자 (!@#$%^&*)");}
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
                    .modifiedAt(member.getModifiedAt())
                    .build();

            return signupInfo;
        }


    }

    @Transactional
    public MemberDto.Response login(MemberDto.Login loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getEmail();
        Member member = isPresentMemberByUsername(username);

        if(member == null){ throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");}

        if(!member.validatePassword(passwordEncoder,loginRequestDto.getPassword())){
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 토큰 발급
        String accessToken = jwtUtil.createToken(username, member.getMemberId(), TokenProperties.AUTH_HEADER);
        String refreshToken = jwtUtil.createToken(username, member.getMemberId(), TokenProperties.REFRESH_HEADER);

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
                .profileUrl(member.getProfileUrl())
                .introduce(member.getIntroduce())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
        return memberResponseDto;
    }

    @Transactional
    public MessageResponseDto logout(HttpServletRequest request, UserDetailsImpl userDetails){

        Member member = userDetails.getMember();

        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);

        if(refreshHeader == null) {
            throw new IllegalArgumentException("Refresh Token이 필요합니다.");}

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)) {
            throw new IllegalArgumentException("유효하지 않은 Token 입니다.");
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
                    return messageResponseDto;
                } else {
                    throw new IllegalArgumentException("유효하지 않은 Token 입니다.");
                }
            default:
                throw new IllegalArgumentException("유효하지 않은 Token 입니다.");
        }
    }

    @Transactional
    public String reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);

        if(refreshHeader == null){
            throw new IllegalArgumentException("Refresh Token이 필요합니다.");
        }

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)){
            throw new IllegalArgumentException("유효하지 않은 Token 입니다.");
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE, "");

        // Refresh 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenValidate) {
            case TokenProperties.EXPIRED:
                throw new IllegalArgumentException("만료된 Refresh Token 입니다.");
            case TokenProperties.VALID:
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                Member member = isPresentMemberByUsername(username);

                if (member == null) {
                    throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                } else {
                    RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                    if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) { // new access token 발급
                        String newAccessToken = jwtUtil.createToken(member.getUsername(), member.getMemberId(), TokenProperties.AUTH_HEADER);
                        response.addHeader(TokenProperties.AUTH_HEADER, TokenProperties.TOKEN_TYPE + newAccessToken);
                        return "토큰이 재발급 되었습니다.";
                    } else {
                        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
                    }
                }
            default:
                throw new IllegalArgumentException("유효하지 않은 Token 입니다.");
        }
    }

    public MemberDto.Response getMemberInfo(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        return MemberDto.Response.builder()
                        .memberId(member.getMemberId())
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .profileUrl(member.getProfileUrl())
                        .introduce(member.getIntroduce())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build();
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
