package com.example.gallery.backend.service;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("회원가입이 성공적으로 수행되야함")
    @Test
    void 회원가입_성공() {
        Map<String, String> params = new HashMap<>();
        params.put("email","testuser@test.com");
        params.put("password","test123456");

        boolean result = accountService.join(params);

        assertTrue(result);

        Member savedMember = memberMapper.findByEmail("testuser@test.com");
        assertNotNull(savedMember);
        assertEquals("testuser@test.com",savedMember.getEmail());
        assertTrue(bCryptPasswordEncoder.matches("test123456",savedMember.getPassword()));
    }

    @DisplayName("회원가입이 실패해야함 - 이메일 중복")
    @Test
    void 회원가입_실패() {
        String email = "admin@admin.com";

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", "admin123456");

        BizException ex = assertThrows(BizException.class, () -> accountService.join(params));
        assertEquals("이미 존재하는 이메일입니다.", ex.getMsg());
    }


    @DisplayName("비밀번호가 변경되야함")
    @Test
    void 비밀번호변경_성공() {
        String email = "testuser@test.com";
        String oldPassword = "test123456";
        String newPassword = "test111111";

        //일단 저장하고 변경해보기로함
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(bCryptPasswordEncoder.encode(oldPassword));
        memberMapper.insert(member);

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", oldPassword);
        params.put("newPassword", newPassword);

        boolean result = accountService.changePassword(params);

        assertTrue(result);

        Member updatedMember = memberMapper.findByEmail(email);
        assertTrue(bCryptPasswordEncoder.matches(newPassword, updatedMember.getPassword()));
    }


    @DisplayName("비밀번호 불일치로 비밀번호 변경이 실패해야함")
    @Test
    void 비밀번호변경_실패() {
        String email = "testuser@test.com";
        String oldPassword = "test111111";

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(bCryptPasswordEncoder.encode(oldPassword));
        memberMapper.insert(member);

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", "wrong111111");
        params.put("newPassword", "test123456");

        BizException ex = assertThrows(BizException.class, () -> accountService.findAllMember());
        assertEquals("비밀번호가 틀렸습니다.", ex.getMsg());
    }

}
