package com.example.miniproject.exception;

import com.example.miniproject.dto.http.DefaultRes;
import com.example.miniproject.dto.http.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class ExceptionAdvisor {

    // IllegalArgumentException & NullPointerException 예외 처리
    @ExceptionHandler(value = {IllegalArgumentException.class,
                               NullPointerException.class,
                               IllegalStateException.class,
                               })
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        String errorMsg = ex.getMessage();

        return ResponseEntity.badRequest().body(DefaultRes.res(StatusCode.BAD_REQUEST, errorMsg));

    }

    // 아이디, 비밀번호 유효성 검사 시 에러
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(DefaultRes.res(StatusCode.BAD_REQUEST, null, errorMap));
    }
}
