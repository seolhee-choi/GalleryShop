package com.example.gallery.backend.interceptor;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public JwtInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getTokenFromRequest(request);  // 요청에서 토큰을 추출하는 메서드

        // 토큰이 없으면 그냥 넘어가게 처리
        if (token == null || token.isEmpty()) {
            return true;  // 인증이 필요없는 요청이므로 인터셉터를 건너뜁니다.
        }

        // 토큰이 있을 경우에는 JWT 검증
        if (!jwtService.isValid(token)) {
            throw new BizException(ErrorCode.ERROR_001);  // 토큰이 유효하지 않으면 예외 처리
        }

        return true;  // 토큰이 유효하면 요청을 처리하도록 계속 진행
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // 요청에서 모든 쿠키를 가져옵니다.
        Cookie[] cookies = request.getCookies();


        // 쿠키가 존재하고, 그 중에서 "token"이라는 이름을 가진 쿠키를 찾아서 반환합니다.
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {  // "token"이라는 이름을 가진 쿠키 찾기
                    return cookie.getValue();  // 쿠키 값 반환
                }
            }
        }


        // 토큰이 없다면 null을 반환
        return null;
    }
}
