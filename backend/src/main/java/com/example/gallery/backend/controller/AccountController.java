package com.example.gallery.backend.controller;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
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
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody Map<String, Object> request, HttpServletResponse response) {
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

            return ResponseFactory.success(null);
        } catch (BadCredentialsException e) {
            throw new BizException(ErrorCode.ERROR_014);
        } catch (UsernameNotFoundException e) {
            throw new BizException(ErrorCode.ERROR_015);
        }

    }

    @PostMapping("/api/account/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return ResponseFactory.success(null);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity<ApiResponse<Map<String, Object>>> check(@AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseFactory.success(null);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", member.getId());
        response.put("email", member.getEmail());
        return ResponseFactory.success(response); // 메시지 생략
    }

    @PostMapping("/api/account/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody Map<String, String> params, HttpServletResponse res) {
        //1. 기존 회원 아이디가 있는지 조회한다.
        Member existingMember = memberMapper.findByEmail(params.get("email"));

        //2-1. 기존 회원 아이디가 있는 경우
        if (existingMember != null) {
            throw new BizException(ErrorCode.ERROR_016);
        }

        //2-2. 기존 회원 아이디가 없는 경우 - db저장
        Member member = new Member();
        member.setEmail(params.get("email"));
        member.setPassword(bCryptPasswordEncoder.encode(params.get("password")));
        memberMapper.insert(member);

        return ResponseFactory.success(null);
    }

    @GetMapping("/api/account/checkEmail")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkDuplicateEmail(@RequestParam String email) {
        boolean exists = memberMapper.existsMemberByEmail(email);
        return ResponseFactory.success(Map.of("exists", exists));

    }


    @PostMapping("/api/account/changePassword")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        String currentPassword = params.get("password");
        String newPassword = params.get("newPassword");

        //1. 회원의 email로 일치하는 회원을 먼저 조회한다.
        Member member = memberMapper.findByEmail(email);

        //1-2. 회원 찾기 불가
        if (member == null ) {
            throw new BizException(ErrorCode.ERROR_015);
        }

        //2-1. 입력한 패스워드 불일치
        if (!bCryptPasswordEncoder.matches(currentPassword, member.getPassword())) {
            throw new BizException(ErrorCode.ERROR_014);
        }

        //2-2. 입력한 패스워드 일치
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        member.setPassword(encodePassword);
        memberMapper.changePassword(member);

        return ResponseFactory.success(null);
    }
}