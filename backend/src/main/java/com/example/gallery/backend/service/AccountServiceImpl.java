package com.example.gallery.backend.service;

import com.example.gallery.backend.entity.Member;
import com.example.gallery.backend.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service("accountService")
public class AccountServiceImpl implements AccountService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    @Override
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if (member != null) {
            JwtService jwtService = new JwtServiceImpl();
            int id = member.getId();
            String token = jwtService.getToken("id", id);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); // front에서 접근 못함
            cookie.setPath("/"); // 모든 경로에 유효함

            res.addCookie(cookie);

//            return ResponseEntity.ok().build();
            return new ResponseEntity<>(id, HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

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
        Member member = memberRepository.findByEmail(params.get("email"));

        //2. 기존 회원 아이디가 없으면 db에 저장
        if (member == null || member.equals("")) {
            Member member1 = new Member();
            member1.setEmail(params.get("email"));
            member1.setPassword(params.get("password"));

            memberRepository.save(member1);

            //2-1.  로그인 메소드를 실행
            return login(params, res);
        } else {
            //3. 기존 회원 아이디가 있으면 중복
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity checkDuplicateEmail(@RequestParam String email) {
        boolean exists = memberRepository.existsMemberByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
