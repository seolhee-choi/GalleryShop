package com.example.gallery.backend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class AccountControllerTest {

    @Test
    void 로그아웃() {

    }

    @Test
    void check() {

    }

    @DisplayName("회원가입이 성공적으로 수행되야함")
    @Rollback(true)
    @Test
    void 회원가입성공() {
        //given
//
//
//        Map<String, String> params = new HashMap<>();
//        params.put("email", "test1@test.com");
//        params.put("password", "test123456");
//
//        HttpServletResponse response = new MockHttpServletResponse();// http환경 구성하기 귀찮아서 쓰는 mock
//
//        //when
//        ResponseEntity result = service.join(params, response);
//
//
//        //then
//        Member saved = service.memberMapper.findByEmail("test1@test.com");
//        Assertions.assertNotNull(saved);
////        assertThat(saved).isNotNull(); //저장된 회원이 실재 존재하는지
//        assertThat(service.bCryptPasswordEncoder.matches("test123456", saved.getPassword())).isTrue();//비번이 올바르게 암호화됐는지
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);//join() 메서드가 리턴한 ResponseEntity의 HTTP상태가 200인지

    }

    @DisplayName("회원가입이 실패되야함 - 이미 존재하는 메일")
    @Test
    void 회원가입실패() {

    }
}
