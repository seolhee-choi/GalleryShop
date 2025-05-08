package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Member;

import java.util.HashMap;
import java.util.Map;

public class InMemoryMemberMapper implements MemberMapper{

    private final Map<String, Member> db = new HashMap<>();

    @Override
    public Member findByEmail(String email) {
        return db.get(email);
    }

    @Override
    public boolean existsMemberByEmail(String email) {
        return db.containsKey(email);
    }

    @Override
    public void insert(Member member) {
        db.put(member.getEmail(), member);
    }

    @Override
    public void changePassword(Member member) {
        db.put(member.getEmail(), member);
    }
}
