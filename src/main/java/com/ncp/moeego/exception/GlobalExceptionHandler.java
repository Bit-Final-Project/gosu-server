package com.ncp.moeego.exception;

import com.ncp.moeego.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 변수 전달
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        String location = getExceptionLocation(e);
        log.error("IllegalArgumentException 발생: {} at {}", e.getMessage(), location);
        ApiResponse<Void> response = ApiResponse.error(
                String.format("잘못된 요청입니다. (%s)", location), "INVALID_ARGUMENT"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // NullPointer
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException e) {
        String location = getExceptionLocation(e);
        log.error("NullPointerException 발생: {} at {}", e.getMessage(), location);
        ApiResponse<Void> response = ApiResponse.error(
                String.format("시스템 오류가 발생했습니다. (%s)", location), "NULL_POINTER"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // 데이터 무결성 제약 조건 위반
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String location = getExceptionLocation(e);
        log.error("DataIntegrityViolationException 발생: {} at {}", e.getMessage(), location);
        ApiResponse<Void> response = ApiResponse.error(
                "데이터 무결성 오류가 발생했습니다.", "DATA_INTEGRITY_VIOLATION"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // RequestParam 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException 발생: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error(
                String.format("필수 요청 파라미터가 누락되었습니다: %s", e.getParameterName()), "MISSING_PARAMETER"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // RequestBody 누락 또는 잘못된 형식
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String location = getExceptionLocation(e);
        log.error("HttpMessageNotReadableException 발생: {} at {}", e.getMessage(), location);
        ApiResponse<Void> response = ApiResponse.error(
                "요청 본문이 누락되었거나 잘못된 형식입니다.", "INVALID_OR_MISSING_REQUEST_BODY"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 클라이언트에서 잘못된 타입의 변수 전달
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String parameterName = e.getName();
        String expectedType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown";
        log.error("MethodArgumentTypeMismatchException 발생: {} for parameter '{}'", e.getMessage(), parameterName);

        ApiResponse<Void> response = ApiResponse.error(
                String.format("잘못된 타입의 요청 파라미터입니다. '%s' 파라미터는 %s 타입이어야 합니다.", parameterName, expectedType),
                "TYPE_MISMATCH"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // HTTP 메서드가 잘못된 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String unsupportedMethod = e.getMethod();
        String supportedMethods = e.getSupportedHttpMethods() != null ? e.getSupportedHttpMethods().toString() : "지원되지 않음";

        log.error("HttpRequestMethodNotSupportedException 발생: {} for method '{}'", e.getMessage(), unsupportedMethod);

        ApiResponse<Void> response = ApiResponse.error(
                String.format("'%s' 메서드는 지원되지 않습니다. 지원 가능한 메서드: %s", unsupportedMethod, supportedMethods),
                "METHOD_NOT_SUPPORTED"
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // 기본 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        String location = getExceptionLocation(e);
        log.error("Exception 발생: {} at {}", e.getMessage(), location);
        ApiResponse<Void> response = ApiResponse.error(
                String.format("예기치 못한 오류가 발생했습니다. (%s)", location), "GENERAL_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // 예외 발생 위치 추적 메서드
    private String getExceptionLocation(Throwable e) {
        StackTraceElement element = e.getStackTrace()[0];
        return String.format("Class: %s, Method: %s, Line: %d",
                element.getClassName(), element.getMethodName(), element.getLineNumber());
    }
}
