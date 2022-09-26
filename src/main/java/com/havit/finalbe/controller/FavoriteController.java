package com.havit.finalbe.controller;

import com.havit.finalbe.dto.FavoriteDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "[즐겨찾기 API]")
@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "그룹 즐겨찾기", description = "해당하는 그룹을 즐겨찾기/즐겨찾기 취소 합니다.")
    @PostMapping("/api/auth/favorite")
    public ResponseDto<?> favorites(@RequestBody FavoriteDto favoriteDto, HttpServletRequest request) {
        return favoriteService.favorites(favoriteDto, request);
    }
}
