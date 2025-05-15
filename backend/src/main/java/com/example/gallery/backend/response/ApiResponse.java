package com.example.gallery.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("000", "요청이 성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("000", "요청이 성공적으로 처리되었습니다.", null);
    }

    public static ApiResponse<Void> of(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
