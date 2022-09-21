package com.havit.finalbe.service;

import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.SubComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@RequiredArgsConstructor
@Component
public class ServiceUtil {

    // 토큰 선언 ex.TokenProvider

    // 멤버 인증
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.tokenValidation(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    // 댓글 작성 날짜 포맷
    public String getDateFormatOfComment(Comment comment){
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
    public String getDateFormatOfSubComment(SubComment subComment){
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
}
