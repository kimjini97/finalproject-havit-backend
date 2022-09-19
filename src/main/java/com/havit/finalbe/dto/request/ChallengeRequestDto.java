package com.havit.finalbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeRequestDto {
    private String title;
    private String leaderName;
    private String crewName;
    private String startDate;

}
