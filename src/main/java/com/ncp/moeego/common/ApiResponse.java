package com.ncp.moeego.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * API 응답을 자동으로 만들어줌
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success; // 성공 여부
    private String message; // 사용자에게 보여줄 메시지
    private String errorCode; // 에러 코드 (실패 시)
    private T data; // 응답 데이터 (성공 시)
    private LocalDateTime timestamp; // 응답 시간


    /**
     * 성공 응답 생성
     * <br>
     * ApiResponse.success(메세지, 데이터)
     *
     * @param message 성공 메시지
     * @param data 응답 데이터
     * @param <T> 응답 데이터의 타입
     * @return 성공 ApiResponse 객체
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 성공 응답 (반환 데이터 없음)
     * <br>
     * ApiResponse.success(메세지)
     *
     * @param message 성공 메시지
     * @return 성공 ApiResponse 객체 (데이터 없음)
     */
    public static ApiResponse<Void> success(String message) {
        return success(message, null);
    }

    /**
     * 실패 응답 생성
     * <br>
     * GlobalExceptionHandler 에서 사용
     *
     * @param message 에러 메시지
     * @param errorCode 에러 코드
     * @return 실패 ApiResponse 객체
     */
    public static ApiResponse<Void> error(String message, String errorCode) {
        return ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

