package com.example.gallery.backend.auth;

import com.example.gallery.backend.dto.Member;
import io.jsonwebtoken.Claims;

public interface JwtService {
//    String getToken(String key, Object value);
    String getToken(Member member);

    Claims getClaims(String token);

    boolean isValid(String token);

    int getId(String token);
}
