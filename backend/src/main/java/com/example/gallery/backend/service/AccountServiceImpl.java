package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.mapper.MemberMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Service("accountService")
public class AccountServiceImpl implements AccountService{

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtService jwtService;

//    아래 login메서드는 LofinFilter로 처리될거라 주석처리
//    @Override
//    public ResponseEntity<?> login(@RequestBody Map<String, String> params, HttpServletResponse res) {
//        Member member = memberMapper.findByEmail(params.get("email"));
//        // 해당하는 계정이 없을 때
//        if (member == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일의 회원이 없습니다.");
//        }
//
//        // 이메일은 있으나 해당 계정의 비밀번호가 틀렸을 때
//        boolean chkPassword = bCryptPasswordEncoder.matches(params.get("password"), member.getPassword());
//        if (!chkPassword) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "비밀번호가 틀렸습니다."));
//        }
//
//        int id = member.getId();
//        String token = jwtService.getToken("id", id);
//
//        Cookie cookie = new Cookie("token", token);
//        cookie.setHttpOnly(true); // front에서 접근 못함
//        cookie.setPath("/"); // 모든 경로에 유효함
//        res.addCookie(cookie);
//
//        return new ResponseEntity<>(id, HttpStatus.OK);
//    }


    @Override
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);

        if(claims != null ) {
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity join(@RequestBody Map<String, String> params, HttpServletResponse res) {
        //1. 기존 회원 아이디가 있는지 조회한다.
        Member member = memberMapper.findByEmail(params.get("email"));

        //2. 기존 회원 아이디가 없으면 db에 저장
        if (member == null || member.equals("")) {
            Member member1 = new Member();
            member1.setEmail(params.get("email"));
            member1.setPassword(bCryptPasswordEncoder.encode(params.get("password")));

            memberMapper.insert(member1);

            //2-1. 프론트에 ok보냄
//            return login(params, res);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            //3. 기존 회원 아이디가 있으면 중복
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity checkDuplicateEmail(@RequestParam String email) {
        boolean exists = memberMapper.existsMemberByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
