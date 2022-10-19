package com.havit.finalbe.util;

import com.havit.finalbe.dto.response.AllGroupListResponseDto;

import java.util.Comparator;

public class ListComparator implements Comparator<AllGroupListResponseDto> {

    @Override
    public int compare(AllGroupListResponseDto o1, AllGroupListResponseDto o2) {

        int memberCount1 = o1.getMemberCount();
        int memberCount2 = o2.getMemberCount();

        // 음수면 왼쪽 정렬, 양수면 오른쪽 정렬
        // 내림차순 정렬 : 큰 수가 왼쪽으로 가야하기 때문에 값 비교 후 큰 수를 왼쪽으로 보내기
        if (memberCount1 > memberCount2) {
            return -1;
        } else if (memberCount1 < memberCount2) {
            return 1;
        }  else {
            return 0;
        }
    }
}
