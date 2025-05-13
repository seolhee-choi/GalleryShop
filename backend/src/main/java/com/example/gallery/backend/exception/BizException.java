package com.example.gallery.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@Getter
public class BizException extends RuntimeException {

    private String msg;
    private String code;
    private HttpStatus httpStatus;

    public BizException(final ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
        this.httpStatus = errorCode.getHttpStatus();
    }

    // 가변인자 사용 불가로 Object 배열로 변경함
    public BizException(final ErrorCode errorCode, final Object[] args) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.httpStatus = errorCode.getHttpStatus();
        String errMsg = errorCode.getMsg();
        this.msg = MessageFormat.format(errMsg, args);
    }
}
