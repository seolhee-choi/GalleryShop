package com.example.gallery.backend.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ErrorResponse {
    private String code;
    private String msg;

    public ErrorResponse(final ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ErrorResponse(final BizException ex) {
        this.code = ex.getCode();
        this.msg = ex.getMsg();
    }
}