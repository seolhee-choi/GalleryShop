package com.example.gallery.backend.response;

import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    private static final String DEFAULT_SUCCESS_CODE = "200";
    private static final String DEFAULT_SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";

    // 메시지 생략 버전
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data));
    }

    // 필요 시 커스텀 메시지를 쓰고 싶을 경우
    public static <T> ResponseEntity<ApiResponse<T>> success(String msg, T data) {
        return ResponseEntity.ok(new ApiResponse<>(DEFAULT_SUCCESS_CODE, msg, data));
    }
}
