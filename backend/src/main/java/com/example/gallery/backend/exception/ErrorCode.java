package com.example.gallery.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Schema(name = "에러_코드값_정의")
@Getter
public enum ErrorCode {
    OK("000", "정상", HttpStatus.OK),

    ERROR_001("001", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    ERROR_002("002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ERROR_003("003", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ERROR_004("004", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    ERROR_005("005", "입력값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    ERROR_006("006", "중복된 요청입니다.", HttpStatus.CONFLICT),
    ERROR_007("007", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_008("008", "지원하지 않는 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    ERROR_009("009", "토큰이 유효하지 않거나 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    ERROR_010("010", "이미 처리된 요청입니다.", HttpStatus.CONFLICT),
    ERROR_011("011", "세션이 만료되었습니다. 다시 로그인 해주세요.", HttpStatus.UNAUTHORIZED),
    ERROR_012("012", "데이터베이스 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_013("013", "장바구니에 상품이 없습니다.", HttpStatus.NOT_FOUND),

    ERROR_014("014", "비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED),
    ERROR_015("015", "존재하지 않는 사용자입니다.", HttpStatus.UNAUTHORIZED),
    ERROR_016("016", "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),
    ERROR_017("017", "json으로 변환하는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_018("01", "주문한 내역이 없습니다.", HttpStatus.NOT_FOUND);


    @Schema(description = "코드값")
    private String code;
    @Schema(description = "결과 메시지")
    private String msg;
    @Schema(description = "HTTP 응답 코드")
    private HttpStatus httpStatus;

    ErrorCode(final String code, final String msg, final HttpStatus httpStatus) {
        this.code = code;
        this.msg = msg;
        this.httpStatus = httpStatus;
    }

    public static ErrorCode findByCode(String code) {
        for (final ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }

}
