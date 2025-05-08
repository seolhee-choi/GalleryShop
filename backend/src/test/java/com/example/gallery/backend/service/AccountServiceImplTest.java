package com.example.gallery.backend.service;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.mapper.InMemoryMemberMapper;
import com.example.gallery.backend.mapper.MemberMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AccountServiceImplTest {


    @Test
    void 로그아웃() {

    }

    @Test
    void check() {

    }

    @DisplayName("회원가입이 성공적으로 수행되야함")
    @Test
    void 회원가입성공() {
        //given
        MemberMapper memberMapper = new InMemoryMemberMapper();// 테스트용으로 만든 Mapper호출
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AccountServiceImpl service = new AccountServiceImpl();

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "test123456");

        HttpServletResponse response = new MockHttpServletResponse();// http환경 구성하기 귀찮아서 쓰는 mock

        //when
        ResponseEntity result = service.join(params, response);


        //then
        Member saved = memberMapper.findByEmail("test@test.com");
        assertThat(saved).isNotNull(); //저장된 회원이 실재 존재하는지
        assertThat(encoder.matches("test123456", saved.getPassword())).isTrue();//비번이 올바르게 암호화됐는지
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);//join() 메서드가 리턴한 ResponseEntity의 HTTP상태가 200인지

    }

    @DisplayName("회원가입이 실패되야함 - 이미 존재하는 메일")
    @Test
    void 회원가입실패() {

    }
}
