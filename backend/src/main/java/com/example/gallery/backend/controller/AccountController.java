package com.example.gallery.backend.controller;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.AccountService;
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
import java.util.List;
import java.util.Map;


@RestController
public class AccountController {

    private final JwtService jwtService;

    private final AccountService accountService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    public AccountController(AuthenticationManager authenticationManager,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             JwtService jwtService,
                             AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    @PostMapping("/api/account/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return ResponseFactory.success(true);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity<ApiResponse<Map<String, Object>>> check(@AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseFactory.success(null);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", member.getId());
        response.put("email", member.getEmail());
        response.put("role", member.getRole());
        return ResponseFactory.success(response); // 메시지 생략
    }

    @PostMapping("/api/account/join")
    public ResponseEntity<ApiResponse<Boolean>> join(@RequestBody Map<String, String> params) {
        return ResponseFactory.success(accountService.join(params));
    }

    @GetMapping("/api/account/checkEmail")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkDuplicateEmail(@RequestParam String email) {
        return ResponseFactory.success(Map.of("exists", accountService.existsMemberByEmail(email)));
    }


    @PostMapping("/api/account/changePassword")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(@RequestBody Map<String, String> params) {
        return ResponseFactory.success(accountService.changePassword(params));
    }

    // 관리자 - 회원 조회
    @GetMapping("/api/admin/members")
    public ResponseEntity<ApiResponse<List<Member>>> memberList() {
        return ResponseFactory.success(accountService.findAllMember());
    }

    // 관리자 - 회원 업데이트
    @PostMapping("/api/admin/members")
    public ResponseEntity<ApiResponse<Boolean>> updateMembers(@RequestBody List<Member> members) {
        return ResponseFactory.success(accountService.updateMember(members));
    }
}