package com.havit.finalbe.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.havit.finalbe.entity.*;
import com.havit.finalbe.jwt.util.JwtUtil;
import com.havit.finalbe.jwt.util.TokenProperties;
import com.havit.finalbe.repository.GroupTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ServiceUtil {

    @Value("${cloud.aws.s3.bucket}")
    private String havitbucket;
    private final AmazonS3Client amazonS3Client;
    private final JwtUtil jwtUtil;
    private final GroupTagRepository groupTagRepository;

    // 멤버 인증
    public Member validateMember(HttpServletRequest request) {
        if (jwtUtil.validateToken(request.getHeader("Refresh-Token")).equals(TokenProperties.INVALID)) {
            return null;
        }
        return jwtUtil.getMemberFromAuthentication();
    }

    // 댓글 작성 날짜 포맷
    public String getDateFormatOfComment(Comment comment) {
        LocalDate curDateTime = LocalDate.from(LocalDateTime.now());
        LocalDate commentCreatedAt = LocalDate.from(comment.getCreatedAt());
        Period period = Period.between(commentCreatedAt,curDateTime);
        String dateFormat = "";
        int days = (period.getDays());
        if(days < 1){
            LocalDateTime curTodayTime = LocalDateTime.now();
            LocalDateTime createdTime = comment.getCreatedAt();
            Duration duration = Duration.between(createdTime,curTodayTime);
            double time = duration.getSeconds();
            double hour = time/3600;
            if(hour < 1){
                double minute = time/60;
                if(minute < 1){
                    dateFormat += "방금 전";
                }
                else{
                    dateFormat += (int)minute;
                    dateFormat += "분 전";
                }
            }else{
                dateFormat += (int)hour;
                dateFormat += "시간 전";
            }
        }else if( days < 8){
            dateFormat += days;
            dateFormat += "일 전";
        }else {
            dateFormat += commentCreatedAt;
        }

        return dateFormat;
    }

    // 대댓글 작성 날짜 포맷
    public String getDateFormatOfSubComment(SubComment subComment) {
        LocalDate curDateTime = LocalDate.from(LocalDateTime.now());
        LocalDate subCommentCreatedAt = LocalDate.from(subComment.getCreatedAt());
        Period period = Period.between(subCommentCreatedAt,curDateTime);
        String dateFormat = "";
        int days = (period.getDays());
        if(days < 1){
            LocalDateTime curTodayTime = LocalDateTime.now();
            LocalDateTime createdTime = subComment.getCreatedAt();
            Duration duration = Duration.between(createdTime,curTodayTime);
            double time = duration.getSeconds();
            double hour = time/3600;
            if(hour < 1){
                double minute = time/60;
                if(minute < 1){
                    dateFormat += "방금 전";
                }
                else{
                    dateFormat += (int)minute;
                    dateFormat += "분 전";
                }
            }else{
                dateFormat += (int)hour;
                dateFormat += "시간 전";
            }
        }else if( days < 8){
            dateFormat += days;
            dateFormat += "일 전";
        }else {
            dateFormat += subCommentCreatedAt;
        }

        return dateFormat;
    }

    // 이미지 업로드 및 URL 변환
    public String uploadImage(MultipartFile multipartFile, String dirName) throws IOException {

        String fileName = dirName + "/" + UUID.randomUUID() + multipartFile.getName();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(multipartFile.getSize());

        // S3 에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(havitbucket, fileName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        // URL 변환
        return amazonS3Client.getUrl(havitbucket, fileName).toString();
    }

    // S3 이미지 객체 삭제
    public void deleteImage(String key) {
        amazonS3Client.deleteObject(havitbucket, key);
    }

    // Group 에 해당하는 TagName 불러오기
    public List<String> getTagNameListFromGroupTag(Groups groups) {
        List<GroupTag> groupTagList = groupTagRepository.findAllByGroups(groups);
        List<String> tagNameList = new ArrayList<>();

        for (GroupTag groupTag : groupTagList) {
            String tagName = groupTag.getTags().getTagName();
            tagNameList.add(tagName);
        }

        return tagNameList;
    }
}
