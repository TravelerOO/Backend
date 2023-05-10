package com.example.miniproject.exception;

import com.example.miniproject.dto.http.DefaultDataRes;
import com.example.miniproject.dto.http.DefaultRes;
import com.example.miniproject.dto.http.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
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

    // CustomException 처리
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        String msg = ex.getMsg();
        int statusCode = ex.getStatusCode();

        return ResponseEntity.status(statusCode).body(new DefaultRes<>(msg));
    }

    // 아이디, 비밀번호 유효성 검사 시 에러
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return ResponseEntity.badRequest().body(new DefaultDataRes<Map<String, String>>(null, errorMap));
    }

    // 이미지 파일 미 업로드 시
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<?> handleBindException(BindException ex) {
        return ResponseEntity.badRequest().body(new DefaultRes<>(StatusCode.BAD_REQUEST, "이미지 파일을 업로드해주세요"));
    }
}
