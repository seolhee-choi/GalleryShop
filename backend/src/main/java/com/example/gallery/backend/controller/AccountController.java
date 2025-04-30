package com.example.gallery.backend.controller;

import com.example.gallery.backend.service.AccountService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class AccountController {

//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    JwtService jwtService;
//
//    @PostMapping("/api/account/login")
//    public ResponseEntity Login(@RequestBody Map<String, String> params, HttpServletResponse res) {
//        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));
//
//        if (member != null) {
//            JwtService jwtService = new JwtServiceImpl();
//            int id = member.getId();
//            String token = jwtService.getToken("id", id);
//
//            Cookie cookie = new Cookie("token", token);
//            cookie.setHttpOnly(true); // front에서 접근 못함
//            cookie.setPath("/"); // 모든 경로에 유효함
//
//            res.addCookie(cookie);
//
////            return ResponseEntity.ok().build();
//            return new ResponseEntity<>(id, HttpStatus.OK);
//        }
//
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//    }
//
//    @PostMapping("/api/account/logout")
//    public ResponseEntity Logout(HttpServletResponse res) {
//        Cookie cookie = new Cookie("token", null);
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//
//        res.addCookie(cookie);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @GetMapping("/api/account/check")
//    public ResponseEntity Check(@CookieValue(value = "token", required = false) String token) {
//        Claims claims = jwtService.getClaims(token);
//
//        if(claims != null ) {
//            int id = Integer.parseInt(claims.get("id").toString());
//            return new ResponseEntity<>(id, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//    @PostMapping("/api/account/join")
//    public ResponseEntity Join(@RequestBody Map<String, String> params, HttpServletResponse res) {
//        //1. 기존 회원 아이디가 있는지 조회한다.
//        Member member = memberRepository.findByEmail(params.get("email"));
//
//        //2. 기존 회원 아이디가 없으면 db에 저장
//        if (member == null) {
//            Member member1 = new Member();
//            member1.setEmail(params.get("email"));
//            member1.setPassword(params.get("password"));
//
//            memberRepository.save(member1);
//
//            //2-1.  로그인 메소드를 실행
//            return Login(params, res);
//        } else {
//            //3. 기존 회원 아이디가 있으면 중복
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }

    @Autowired
    AccountService accountService;

//    @PostMapping("/api/account/login")
//    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
//        return accountService.login(params, res);
//    }

    @PostMapping("/api/account/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        return accountService.logout(res);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        return accountService.check(token);
    }

    @PostMapping("/api/account/join")
    public ResponseEntity join(@RequestBody Map<String, String> params, HttpServletResponse res) {
        return accountService.join(params, res);
    }
    @GetMapping("/api/account/checkEmail")
    public ResponseEntity checkDuplicateEmail(@RequestParam String email) {
        return accountService.checkDuplicateEmail(email);
    }
}