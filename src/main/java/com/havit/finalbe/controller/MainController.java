package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MainService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"메인 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/main")
public class MainController {

    private final MainService mainService;

    @Operation(summary = "그룹 검색", description = "그룹 제목, 그룹 작성자 닉네임으로 전체 그룹을 검색합니다.")
    @GetMapping("/search")
    public ResponseDto<?> searchGroup(@RequestParam(value = "search") String searchWord,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainService.searchGroup(searchWord, userDetails);
    }
}