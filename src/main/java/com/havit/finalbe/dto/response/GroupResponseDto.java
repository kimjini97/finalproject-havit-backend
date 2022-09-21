package com.havit.finalbe.dto.response;

import com.havit.finalbe.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {
    private Long groupId;
    private String title;
    private String nickname;
    private String leaderName;
    private String crewName;
    private String startDate;
    private String content;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> groupTag;
    private int memberCount;
    private List<Member> memberList;
    private List<String> certifyImgUrlList;
    private boolean favorite;
}