package com.example.gallery.backend.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AccountService {
//    ResponseEntity<?> login(Map<String, String> params, HttpServletResponse res);
    ResponseEntity<?> logout(HttpServletResponse res);
    ResponseEntity<?> check(String token);
    ResponseEntity<?> join(Map<String, String> params, HttpServletResponse res);
    ResponseEntity<?> checkDuplicateEmail(String params);
    ResponseEntity<?> changePassword(Map<String, String> params, HttpServletResponse res);
}
