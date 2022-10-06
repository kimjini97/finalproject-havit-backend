package com.havit.finalbe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND,"데이터가 존재하지 않습니다."),
    INVALID_ERROR(HttpStatus.BAD_REQUEST,"에러 발생"),

    //회원가입, 로그인 관련 에러
    EMPTY_VALUE(HttpStatus.NOT_FOUND, "정보를 입력하세요."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "필드는 100자 이하여야 하며, @ 기호 전까지 64자 이하여야 합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 최소 8자 이상 , 소문자 , 숫자 (0-9) 또는 특수문자 (!@#$%^&*)"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일 주소가 있습니다."),

    // 회원 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 권한 요청 시 Access 토큰을 보내지 않은 경우
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // 로그아웃시 refresh 토큰을 보내지 않은 경우
    NEED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"Refresh Token이 필요합니다."),

    // 유효하지 않은 토큰
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Token 입니다."),

    // 일치하지 않은 토큰
    REFRESH_TOKEN_NOT_MATCHED(HttpStatus.BAD_REQUEST, "토큰이 일치하지 않습니다."),

    // 만료된 토큰
    EXPIRED_ACCESS_TOKEN(HttpStatus.NOT_FOUND, "만료된 Access Token 입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "만료된 Refresh Token 입니다."),

    // 그룹, 댓글
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 그룹을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    CERTIFY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 인증샷을 찾을 수 없습니다."),
    MEMBER_NOT_MATCHED(HttpStatus.NOT_FOUND, "작성자가 아닙니다."),

    // 태그
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다."),

    // 참여하기
    DUPLICATE_PARTICIPATION(HttpStatus.BAD_REQUEST, "이미 참여 중인 그룹입니다."),
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "참여 내역이 없습니다."),

    // 이미지
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "삭제할 이미지가 없습니다."),
    NO_IMAGE_ATTACHED(HttpStatus.NOT_FOUND, "첨부된 이미지가 없습니다."),

    // 메인 : 참여하지 않은 사람 목록
    CHALLENGERS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 그룹의 참여자가 없습니다.");
    /* 400 BAD_REQUEST : 잘못된 요청 */
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    //
    /* INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    /*USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),

    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    /*DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    ;*/


    private final HttpStatus httpStatus;
    private final String message;
}
