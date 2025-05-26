package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Search;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public String getTokenByEmail(Member memberDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberDto.getEmail(), memberDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Member member = memberMapper.findByEmail(memberDto.getEmail());
        return jwtService.getToken(member);
    }

    public boolean join(Map<String, String> params) {
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

        return true;
    }

    public boolean existsMemberByEmail(String email) {
        return memberMapper.existsMemberByEmail(email);
    }

    public boolean changePassword(Map<String, String> params) {
        String email = params.get("email");
        String currentPassword = params.get("password");
        String newPassword = params.get("newPassword");

        //1. 회원의 email로 일치하는 회원을 먼저 조회한다.
        Member member = memberMapper.findByEmail(email);

        //1-2. 회원 찾기 불가
        if (member == null ) {
            throw new BizException(ErrorCode.ERROR_015);
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_020);
        }

        //2-1. 입력한 패스워드 불일치
        if (!bCryptPasswordEncoder.matches(currentPassword, member.getPassword())) {
            throw new BizException(ErrorCode.ERROR_014);
        }

        //2-2. 입력한 패스워드 일치하면 새로운 암호를 암호화함
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        member.setPassword(encodePassword);
        memberMapper.changePassword(member);

        return true;
    }

    public PageResponse<Member> findAllMember(Search search) {
        int totalCount = memberMapper.countMember(search);
        List<Member> members = memberMapper.findAllMember(search);

        if(members.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_007);
        }

        int totalPage = (int)Math.ceil((double) totalCount / search.getRecordSize());

        return new PageResponse<>(members, totalCount, search.getPage(), totalPage);
//        return members;
    }

    public boolean updateMember(List<Member> members) {
        for (Member m : members) {
            memberMapper.updateMember(m);
        }

        return true;
    }
}
