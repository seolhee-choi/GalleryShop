package com.example.gallery.backend.auth;

import com.example.gallery.backend.dto.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

//    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public LoginFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtService = jwtService;
        super(new AntPathRequestMatcher("/api/account/login"));
        setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 데이터를 Map으로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);

            String email = requestMap.get("email");
            String password = requestMap.get("password");

            // 스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);


            // token에 담은 검증을 위한 AuthenticationManager에게 전달 → UserDetailsService 호출됨
//            return authenticationManager.authenticate(authToken);
            return getAuthenticationManager().authenticate(authToken);  // getAuthenticationManager() 사용

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication authentication) throws IOException {
        Member member = (Member) authentication.getPrincipal();
//        String token = jwtService.getToken("id", member.getId());
        String token = jwtService.getToken(member);

        // 로그 추가: 토큰 발급 확인
        logger.info("*****Successfully authenticated for user: " + member.getEmail());
        logger.debug("*****JWT Token: " + token);  // 실제로 발급된 JWT 로그

        // 현재 인증 정보를 SecurityContext에 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //JWT를 쿠키에 담아 응답
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        res.addCookie(cookie);

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
//        res.getWriter().write("{\"id\": " + member.getId() + ", \"email\": \"" + member.getEmail() + "\"}");

        //front쪽에 JSON 객체로 응답하는 과정
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", member.getId());
        responseBody.put("email", member.getEmail());

        ObjectMapper mapper = new ObjectMapper();
        res.getWriter().write(mapper.writeValueAsString(responseBody));

        //로그인 성공 상태 코드
        logger.info("로그인 성공 - JWT 발급 완료");

    }


    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                              AuthenticationException failed) throws IOException{

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"error\": \"이메일 또는 비밀번호가 틀렸습니다.\"}");

        // 실패 이유 로그 출력
        logger.error("*****Login failed: " + failed.getMessage());
        logger.info("로그인 실패 -  JWT 발급 실패" );

    }
}
