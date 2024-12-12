package com.ncp.moeego.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success; // 성공 여부
    private String message; // 사용자에게 보여줄 메시지
    private String errorCode; // 에러 코드 (실패 시)
    private T data; // 응답 데이터 (성공 시)
    private LocalDateTime timestamp; // 응답 시간


    // 성공 응답 생성
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 (반환할 데이터가 없는 경우)
    public static ApiResponse<Void> success(String message) {
        return success(message, null);
    }

    // 에러
    public static ApiResponse<Void> error(String message, String errorCode) {
        return ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

