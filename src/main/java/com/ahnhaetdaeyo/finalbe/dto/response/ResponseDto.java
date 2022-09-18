package com.ahnhaetdaeyo.finalbe.dto.response;

import com.ahnhaetdaeyo.finalbe.exception.ErrorMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private T data;
    private ErrorMsg errorMsg;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data, null);
    }
    public static <T> ResponseDto<T> fail(ErrorMsg code) {
        return new ResponseDto<>(null, code);
    }
}
