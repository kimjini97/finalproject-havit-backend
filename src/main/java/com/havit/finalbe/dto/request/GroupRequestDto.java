package com.havit.finalbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequestDto {
    private String title;
    private String leaderName;
    private String crewName;
    private String startDate;
    private String content;
    private List<String> groupTag;
    private MultipartFile imgFile;
    private String deadline;
}
