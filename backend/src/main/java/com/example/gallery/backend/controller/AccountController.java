package com.example.gallery.backend.controller;

import com.example.gallery.backend.service.AccountService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

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

    @PostMapping("/api/account/changePassword")
    public ResponseEntity changePassword(@RequestBody Map<String, String> params, HttpServletResponse res) {
        return accountService.changePassword(params, res);
    }

}