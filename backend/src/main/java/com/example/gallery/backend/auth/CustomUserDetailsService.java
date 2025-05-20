package com.example.gallery.backend.auth;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.response.ResponseFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    public CustomUserDetailsService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberMapper.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("해당 이메일의 유저를 찾을 수 없습니다." + email);
        }

        return member;
    }

}
