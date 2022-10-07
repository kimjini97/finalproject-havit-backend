package com.havit.finalbe.exception;

import com.havit.finalbe.dto.response.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice // @Controller 전역에서 발생할 수 있는 예외를 잡아 처리해 주는 어노테이션이다.
public class ValidException {

    // @valid  유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDto> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        MessageResponseDto messageResponseDto = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<>(messageResponseDto, HttpStatus.BAD_REQUEST);
    }
    
    private MessageResponseDto makeErrorResponse(BindingResult bindingResult) {
        String code = "";
        String description = "";

        // 에러가 있다면
        if (bindingResult.hasErrors()) {

            // DTO 에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();

            switch (bindResultCode) {
                case "NotNull" :
                case "NotEmpty" :
                case "NotBlank" :
                    code = bindResultCode;
                    // DTO 에 설정한 message 값을 가져온다.
                    description = bindingResult.getFieldError().getDefaultMessage();
                    break;
            }
        }

        return MessageResponseDto.builder()
                .errorcode(code)
                .message(description)
                .build();
    }
}
