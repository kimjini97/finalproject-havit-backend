package com.havit.finalbe.service;

import com.havit.finalbe.dto.MemberDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.exception.*;
import com.havit.finalbe.repository.MemberRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseDto<?> checkPassword(MemberDto.CheckPassword checkPasswordDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        if(!member.validatePassword(passwordEncoder, checkPasswordDto.getPassword())) {
            return ResponseDto.success("false");
        }

        return ResponseDto.success("true");
    }

    @Transactional
    public ResponseDto<?> editMyInfo(MemberDto.MyPage myPageDto, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

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

        String introduce = myPageDto.getIntroduce();
        if (null == introduce || introduce.isEmpty()) {
            if (null == findMember.getIntroduce()) {
                introduce = null;
            } else if (!findMember.getIntroduce().isEmpty()) {
                introduce = findMember.getIntroduce();
            }
        }

        String password = myPageDto.getPassword();
        String passwordConfirm = myPageDto.getPasswordConfirm();
        if (password.isEmpty()) {
            password = member.getPassword();
            findMember.edit(imgUrl, nickname, introduce, password);

            return ResponseDto.success(
                    MemberDto.Response.builder()
                            .memberId(findMember.getMemberId())
                            .username(findMember.getUsername())
                            .nickname(findMember.getNickname())
                            .profileUrl(findMember.getProfileUrl())
                            .introduce(findMember.getIntroduce())
                            .createdAt(findMember.getCreatedAt())
                            .modifiedAt(findMember.getModifiedAt())
                            .build()
            );
        }

        if (!passwordStrCheck(password)) {
            throw new InvalidPasswordException(ErrorMsg.INVALID_PASSWORD);
        } else if (!password.equals(passwordConfirm)) {
            return ResponseDto.fail(PASSWORD_NOT_MATCHED);
        } else {
            findMember.edit(imgUrl, nickname, introduce, passwordEncoder.encode(password));
        }

        return ResponseDto.success(
                MemberDto.Response.builder()
                        .memberId(findMember.getMemberId())
                        .username(findMember.getUsername())
                        .nickname(findMember.getNickname())
                        .profileUrl(findMember.getProfileUrl())
                        .introduce(findMember.getIntroduce())
                        .createdAt(findMember.getCreatedAt())
                        .modifiedAt(findMember.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteProfile(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

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
                        .introduce(findMember.getIntroduce())
                        .createdAt(findMember.getCreatedAt())
                        .modifiedAt(findMember.getModifiedAt())
                        .build()
        );
    }

    private boolean passwordStrCheck (String password) {
        return Pattern.matches("^(?=.*\\d)[a-z\\d!@#$%^&*]{8,}$", password);
    }
}
