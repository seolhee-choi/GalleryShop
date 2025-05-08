package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class Member implements UserDetails {

    private int id;

    private String email;

    private String password;

    private String newPassword;

    private String role;

    // UserDetails 인터페이스 구현(Spring Security)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role; // ✅ this.role로 바로 접근
            }
        });

        // 예시로 "ROLE_USER" 권한 부여
        return collection;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 만료되지 않은 계정
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 잠기지 않은 계정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명이 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; // 활성화된 계정
    }

}
