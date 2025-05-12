package com.example.gallery.backend.dto;

public class ResultVO<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(200, "성공", data);
    }

    public static <T> ResultVO<T> error(String message) {
        return new ResultVO<>(500, message, null);
    }

    public ResultVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
