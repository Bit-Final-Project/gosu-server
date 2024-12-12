package com.ncp.moeego.exception;

import com.ncp.moeego.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 변수 전달
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException 발생: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("잘못된 요청입니다.", "INVALID_ARGUMENT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // NullPointer
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException 발생: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("시스템 오류가 발생했습니다.", "NULL_POINTER");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // 데이터 무결성 제약 조건 위반
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException 발생: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("데이터 무결성 오류가 발생했습니다.", "DATA_INTEGRITY_VIOLATION");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // 기본 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception 발생: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error("예기치 못한 오류가 발생했습니다.", "GENERAL_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
