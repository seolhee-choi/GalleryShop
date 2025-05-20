package com.example.gallery.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@WebMvcTest(AccountController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("로그인 성공 시 JWT 토큰이 쿠키에 담겨 반환되어야 함")
    @Transactional
    @Rollback // DB에 영향 안 주도록 롤백
    @Test
    void 로그인성공_토큰발급() throws Exception {
        // Given: 회원가입 먼저 시키기 (테스트용 회원)
        String email = "testuser@test.com";
        String password = "test123456";

        // When: 회원가입 요청
        mockMvc.perform(post("/api/account/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new JoinRequest(email, password)
                        )))
                .andExpect(status().isOk());

        // When: 로그인 요청
        mockMvc.perform(post("/api/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequest(email, password)
                        )))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token")) // JWT 쿠키 존재 확인
                .andExpect(cookie().httpOnly("token", true)) // HttpOnly 옵션 확인
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."));
    }

    // 내부 클래스: JoinRequest DTO
    static class JoinRequest {
        public String email;
        public String password;

        public JoinRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    // 내부 클래스: LoginRequest DTO
    static class LoginRequest {
        public String email;
        public String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
