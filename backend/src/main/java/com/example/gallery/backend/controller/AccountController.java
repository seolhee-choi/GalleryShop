package com.example.gallery.backend.controller;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.ResultVO;
import com.example.gallery.backend.mapper.MemberMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AccountController {

    @Autowired
    JwtService jwtService;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AccountController(AuthenticationManager authenticationManager,
                             JwtService jwtService,
                             MemberMapper memberMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.memberMapper = memberMapper;
    }

    @PostMapping("/api/account/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = memberMapper.findByEmail(email);
            String token = jwtService.getToken(member);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(1800); // 30분
            response.addCookie(cookie);

            return ResponseEntity.ok("로그인 성공");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("비밀번호가 틀렸습니다.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("존재하지 않는 사용자입니다.");
        }

    }

    @PostMapping("/api/account/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/account/check")
//    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
//        Claims claims = jwtService.getClaims(token);
//
//        if(claims != null ) {
//            int id = Integer.parseInt(claims.get("id").toString());
//            return new ResponseEntity<>(id, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }

    public ResponseEntity<?> check(@AuthenticationPrincipal Member member) {
        if (member != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", member.getId());
            response.put("email", member.getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/api/account/join")
    public ResponseEntity join(@RequestBody Map<String, String> params, HttpServletResponse res) {
        //1. 기존 회원 아이디가 있는지 조회한다.
        Member member = memberMapper.findByEmail(params.get("email"));

        //2. 기존 회원 이메일이 없으면 db에 저장
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

    @GetMapping("/api/account/checkEmail")
    public ResponseEntity checkDuplicateEmail(@RequestParam String email) {
        boolean exists = memberMapper.existsMemberByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/api/account/changePassword")
    public ResponseEntity changePassword(@RequestBody Map<String, String> params, HttpServletResponse res) {
        //1. 회원의 email로 일치하는 회원을 먼저 조회한다.
        Member member = memberMapper.findByEmail(params.get("email"));

        //1-2. 일치하는 회원이 없으면 에러 반환
        if (member == null || member.getEmail().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        //2. 1에서 찾은 회원의 password와 사용자로부터 입력받은 password가 일치하는지 확인
        if (bCryptPasswordEncoder.matches(params.get("password"), member.getPassword())) {
            //2-1. 일치하면 newPassword를 인코딩
            String encodePassword = bCryptPasswordEncoder.encode(params.get("newPassword"));
            //2-2. 인코딩한 새 비밀번호를 member에 설정
            member.setPassword(encodePassword);
            //2-3. 비밀번호 변경 실행
            memberMapper.changePassword(member);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            //3. 기존 비밀번호가 일치하지 않으면 에러 반환("기존 비밀번호가 다릅니다")
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}