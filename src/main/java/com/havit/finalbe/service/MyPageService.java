package com.havit.finalbe.service;

import com.havit.finalbe.dto.MemberDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.exception.*;
import com.havit.finalbe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final MemberRepository memberRepository;
    private final ServiceUtil serviceUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDto<?> checkPassword(MemberDto.CheckPassword checkPasswordDto, HttpServletRequest request) {

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        if(!member.validatePassword(passwordEncoder, checkPasswordDto.getPassword())) {
            return ResponseDto.fail(PASSWORD_NOT_MATCHED);
        }

        return ResponseDto.success("비밀번호가 일치합니다.");
    }

    @Transactional
    public ResponseDto<?> editMyInfo(MemberDto.MyPage myPageDto, HttpServletRequest request) throws IOException {

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        String imgUrl = "";
        MultipartFile imgFile = myPageDto.getImgFile();
        if (null == imgFile || imgFile.isEmpty()) {
            if (null == findMember.getProfileUrl()) {
                imgUrl = null;
            } else if (!findMember.getProfileUrl().isEmpty()) {
                imgUrl = findMember.getProfileUrl();
            }
        } else {
            imgUrl = serviceUtil.uploadImage(imgFile, "profile");
        }

        String nickname = myPageDto.getNickname();
        if (nickname.isEmpty()) {
            nickname = member.getNickname();
        }

        String password = myPageDto.getPassword();
        String passwordConfirm = myPageDto.getPasswordConfirm();
        if (password.isEmpty()) {
            password = member.getPassword();
            findMember.edit(imgUrl, nickname, password);

            return ResponseDto.success(
                    MemberDto.Response.builder()
                            .memberId(findMember.getMemberId())
                            .username(findMember.getUsername())
                            .nickname(findMember.getNickname())
                            .profileUrl(findMember.getProfileUrl())
                            .build()
            );
        }

        if (!passwordStrCheck(password)) {
            throw new InvalidPasswordException(ErrorMsg.INVALID_PASSWORD);
        } else if (!password.equals(passwordConfirm)) {
            return ResponseDto.fail(PASSWORD_NOT_MATCHED);
        } else {
            findMember.edit(imgUrl, nickname, passwordEncoder.encode(password));
        }

        return ResponseDto.success(
                MemberDto.Response.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .profileUrl(findMember.getProfileUrl())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteProfile(HttpServletRequest request) {

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member findMember = memberRepository.findMemberByMemberId(member.getMemberId());

        String originFile = findMember.getProfileUrl();
        if (null == originFile) {
            return ResponseDto.fail(IMAGE_NOT_FOUND);
        }
        String key = originFile.substring(52);
        serviceUtil.deleteImage(key);

        findMember.deleteImg(null);

        return ResponseDto.success(
                MemberDto.Response.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .profileUrl(findMember.getProfileUrl())
                        .build()
        );
    }

    private boolean passwordStrCheck (String password) {
        return Pattern.matches("^(?=.*\\d)[a-z\\d!@#$%^&*]{8,}$", password);
    }
}
