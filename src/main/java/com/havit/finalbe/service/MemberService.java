package com.havit.finalbe.service;

import com.havit.finalbe.dto.request.LoginRequestDto;
import com.havit.finalbe.dto.request.SignupRequestDto;
import com.havit.finalbe.dto.response.*;
import com.havit.finalbe.entity.*;
import com.havit.finalbe.exception.*;
import com.havit.finalbe.jwt.util.JwtUtil;
import com.havit.finalbe.jwt.util.TokenProperties;
import com.havit.finalbe.repository.*;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final CertifyRepository certifyRepository;
    private final ParticipateRepository participateRepository;
    private final FavoriteRepository favoriteRepository;
    private final EmitterRepository emitterRepository;
    private final ServiceUtil serviceUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    public MemberResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();

//        if (!emailStrCheck(username)) {throw new CustomException(ErrorCode.INVALID_EMAIL);}
        if (!emailDuplicateCheck(username)) {throw new CustomException(ErrorCode.DUPLICATE_EMAIL);}
//        if (!passwordStrCheck(password)) {throw new CustomException(ErrorCode.INVALID_PASSWORD);}
        else {

            Member member = Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .build();
            memberRepository.save(member);

            MemberResponseDto signupInfo = MemberResponseDto.builder()
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
    public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getEmail();
        Member member = isPresentMemberByUsername(username);

        if(member == null){ throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);}


        if(!member.validatePassword(passwordEncoder,loginRequestDto.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
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

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .imageId(member.getImageId())
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
            throw new CustomException(ErrorCode.NEED_REFRESH_TOKEN);}

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE,"");

        // 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenValidate) {
            case TokenProperties.VALID:
            case TokenProperties.EXPIRED:
                RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) {
                    emitterRepository.deleteAllEmittersStartWithMemberId(String.valueOf(member.getMemberId()));
                    emitterRepository.deleteAllEventCacheStartWithMemberId(String.valueOf(member.getMemberId()));
                    refreshTokenRepository.delete(refreshTokenFromDB);
                    MessageResponseDto messageResponseDto = MessageResponseDto.builder()
                            .message("로그아웃 되었습니다.")
                            .build();
                    return messageResponseDto;
                } else {
                    throw new CustomException(ErrorCode.INVALID_TOKEN);
                }
            default:
                throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Transactional
    public String reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);

        if(refreshHeader == null){
            throw new CustomException(ErrorCode.NEED_REFRESH_TOKEN);
        }

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE, "");

        // Refresh 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenValidate) {
            case TokenProperties.EXPIRED:
                throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
            case TokenProperties.VALID:
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                Member member = isPresentMemberByUsername(username);

                if (member == null) {
                    throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                } else {
                    RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                    if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) { // new access token 발급
                        String newAccessToken = jwtUtil.createToken(member.getUsername(), member.getMemberId(), TokenProperties.AUTH_HEADER);
                        response.addHeader(TokenProperties.AUTH_HEADER, TokenProperties.TOKEN_TYPE + newAccessToken);
                        return "토큰이 재발급 되었습니다.";
                    } else {
                        throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_MATCHED);
                    }
                }
            default:
                throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public MemberResponseDto getMyInfo(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        List<Certify> myCertifyList = certifyRepository.findAllByMember_MemberId(member.getMemberId());
        List<CertifyResponseDto> certifyResponseDtoList = new ArrayList<>();

        for (Certify certify : myCertifyList) {
            certifyResponseDtoList.add(
                    CertifyResponseDto.builder()
                            .certifyId(certify.getCertifyId())
                            .groupId(certify.getGroups().getGroupId())
                            .title(certify.getTitle())
                            .imageId(certify.getImageId())
                            .longitude(certify.getLongitude())
                            .latitude(certify.getLatitude())
                            .nickname(certify.getMember().getNickname())
                            .profileImageId(certify.getMember().getImageId())
                            .createdAt(certify.getCreatedAt())
                            .modifiedAt(certify.getModifiedAt())
                            .build()
            );
        }

        return MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .imageId(member.getImageId())
                        .introduce(member.getIntroduce())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .certifyList(certifyResponseDtoList)
                        .build();
    }

    public MemberProfileResponseDto getMemberInfo(Long memberId) {

        Member member = memberRepository.findMemberByMemberId(memberId);

        List<Groups> myGroups = new ArrayList<>();
        List<AllGroupListResponseDto> allMyGroupList = new ArrayList<>();

        List<Participate> myParticipation = participateRepository.findAllByMember_MemberId(memberId);

        for (Participate participate : myParticipation) {
            Groups myJoinGroups = groupRepository.findByGroupId(participate.getGroups().getGroupId());
            myGroups.add(myJoinGroups);
        }

        for (Groups groups : myGroups) {
            boolean isFavorites = false;
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository
                    .findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            AllGroupListResponseDto MyGroupDto = AllGroupListResponseDto.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imageId(groups.getImageId())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            allMyGroupList.add(MyGroupDto);
        }

        List<Certify> myCertifyList = certifyRepository.findAllByMember_MemberId(memberId);
        List<CertifyResponseDto> certifyResponseDtoList = new ArrayList<>();

        for (Certify certify : myCertifyList) {
            certifyResponseDtoList.add(
                    CertifyResponseDto.builder()
                            .certifyId(certify.getCertifyId())
                            .groupId(certify.getGroups().getGroupId())
                            .title(certify.getTitle())
                            .imageId(certify.getImageId())
                            .longitude(certify.getLongitude())
                            .latitude(certify.getLatitude())
                            .nickname(certify.getMember().getNickname())
                            .profileImageId(certify.getMember().getImageId())
                            .createdAt(certify.getCreatedAt())
                            .modifiedAt(certify.getModifiedAt())
                            .build()
            );
        }

        return MemberProfileResponseDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .imageId(member.getImageId())
                .introduce(member.getIntroduce())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .groupList(allMyGroupList)
                .certifyList(certifyResponseDtoList)
                .build();
    }

    private boolean emailDuplicateCheck(String email){
        Object member = isPresentMemberByUsername(email);
        return member == null;
    }

    // 회원가입, 로그인 조건 검증

//    private boolean emailStrCheck (String email){
////        return Pattern.matches("^[a-zA-Z0-9]{1,64}+@[a-zA-Z0-9]{1,100}+$", email);
//        return Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){1,64}@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", email);
//    }
//
//    private boolean passwordStrCheck (String password){
//        return Pattern.matches("^(?=.*\\d)[a-z\\d!@#$%^&*]{8,}$", password);
//    }

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
