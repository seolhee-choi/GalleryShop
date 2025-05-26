package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtServiceImplTest {

    @Autowired
    private JwtService jwtService;
    private String secretKey = "asdfgbcx!!!dbsdbsdb@@3ssdvsbzxcvqweplnsdta2365709"; // 실제 키와 동일하게 세팅


    @Test
    void 토큰생성_getToken() {
        Member member = new Member();
        member.setId(1);
        member.setEmail("testuser@test.com");
        member.setRole("USER");

        String token = jwtService.getToken(member);
        assertNotNull(token); //token이 null이 아니어야함

        // 토큰을 파싱해서 Claims 확인
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(1, claims.get("id", Integer.class).longValue());
        assertEquals("testuser@test.com", claims.get("email", String.class));
        assertEquals("USER", claims.get("role", String.class));

        // 만료 시간은 현재 시간보다 미래여야 한다
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void 정상토큰일때_getClaims_성공() {
        // given
        Member member = new Member();
        member.setId(123);
        member.setEmail("claims@test.com");
        member.setRole("USER");

        String token = jwtService.getToken(member);

        // when
        Claims claims = jwtService.getClaims(token);

        // then
        assertNotNull(claims);
        assertEquals(123, claims.get("id", Integer.class).intValue());
        assertEquals("claims@test.com", claims.get("email", String.class));
        assertEquals("USER", claims.get("role", String.class));
    }

    @Test
    void 유효하지않은토큰일때_getClaims_null반환() {
        // given
        String invalidToken = "this.is.not.a.valid.jwt";

        // when
        Claims claims = jwtService.getClaims(invalidToken);

        // then
        assertNull(claims);
    }

}
