package com.example.gallery.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> bizExceptionHandler(final BizException ex) {
        StackTraceElement traceEl = ex.getStackTrace()[0];
        log.error("BizException occurred in class: {}:{}\nmessage : [ErrorCode:{}]{}",
                traceEl.getClassName(), traceEl.getLineNumber(), ex.getCode(), ex.getMsg());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ErrorResponse(ex));
    }

}
