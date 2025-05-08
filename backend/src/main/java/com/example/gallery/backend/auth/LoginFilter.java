package com.example.gallery.backend.auth;

import com.example.gallery.backend.dto.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
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

            logger.info("Attempting authentication for email: " + email);

        // 스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);


        // token에 담은 검증을 위한 AuthenticationManager에게 전달 → UserDetailsService 호출됨
        return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication authentication) throws IOException {
        Member member = (Member) authentication.getPrincipal();
        String token = jwtService.getToken("id", member.getId());

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
        System.out.println("로그인 성공 (JWT 발급 완료)!!!!!!!");

    }


    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                              AuthenticationException failed) throws IOException{

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"error\": \"이메일 또는 비밀번호가 틀렸습니다.\"}");

        System.out.println("로그인 실패!!!!!!!" );

    }
}
